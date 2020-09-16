package $group__.ui.mvvm.views.components;

import $group__.ui.core.binding.IUIPropertyMappingValue;
import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.IUIComponentManager;
import $group__.ui.core.mvvm.views.components.IUIComponentShapeAnchorController;
import $group__.ui.core.mvvm.views.components.IUIViewComponent;
import $group__.ui.core.mvvm.views.components.extensions.caches.IUIExtensionCache;
import $group__.ui.core.mvvm.views.components.paths.IUIComponentPathResolver;
import $group__.ui.core.mvvm.views.events.IUIEventTarget;
import $group__.ui.core.mvvm.views.paths.IPathResolver;
import $group__.ui.core.parsers.components.UIViewComponentConstructor;
import $group__.ui.core.structures.IAffineTransformStack;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.ui.events.bus.UIEventBusEntryPoint;
import $group__.ui.mvvm.views.UIView;
import $group__.ui.mvvm.views.components.extensions.caches.UIExtensionCache;
import $group__.ui.mvvm.views.components.paths.UIComponentPathResolver;
import $group__.ui.mvvm.views.events.bus.EventUIComponentHierarchyChanged;
import $group__.ui.structures.AffineTransformStack;
import $group__.utilities.CapacityUtilities;
import $group__.utilities.CastUtilities;
import $group__.utilities.ThrowableUtilities;
import $group__.utilities.TreeUtilities;
import $group__.utilities.binding.core.IBinderAction;
import $group__.utilities.binding.core.fields.IBindingField;
import $group__.utilities.binding.core.methods.IBindingMethod;
import $group__.utilities.binding.core.traits.IHasBinding;
import $group__.utilities.collections.MapUtilities;
import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.events.EventBusUtilities;
import $group__.utilities.extensions.IExtensionContainer;
import $group__.utilities.functions.FunctionalUtilities;
import $group__.utilities.functions.IConsumer3;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.reactive.DisposableObserverAuto;
import $group__.utilities.structures.NamespacePrefixedString;
import $group__.utilities.structures.Registry;
import com.google.common.collect.*;
import io.reactivex.rxjava3.core.ObservableSource;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.geom.Point2D;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UIViewComponent<S extends Shape, M extends IUIComponentManager<S>>
		extends UIView<S>
		implements IUIViewComponent<S, M> {
	protected final Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings;
	protected final IUIComponentPathResolver<IUIComponent> pathResolver = new InnerPathResolver();
	protected final IUIComponentShapeAnchorController shapeAnchorController = new UIComponentShapeAnchorController();
	@Nullable
	protected M manager;

	@SuppressWarnings("ThisEscapedInObjectConstruction")
	@UIViewComponentConstructor(type = UIViewComponentConstructor.EnumConstructorType.MAPPINGS)
	public UIViewComponent(Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings) {
		this.mappings = MapUtilities.newMapMakerSingleThreaded().initialCapacity(mappings.size()).makeMap();
		this.mappings.putAll(mappings);

		IExtensionContainer.addExtensionChecked(this, new UIExtensionCache());
	}

	@Override
	public IUIEventTarget getTargetAtPoint(Point2D point) { return IPathResolver.getTargetAtPoint(getPathResolver(), point); }

	@Override
	public Optional<? extends IUIEventTarget> changeFocus(@Nullable IUIEventTarget currentFocus, boolean next) {
		@Nullable Optional<? extends IUIEventTarget> ret = CastUtilities.castChecked(IUIComponent.class, currentFocus)
				.flatMap(cf ->
						CacheViewComponent.CHILDREN_FLAT_FOCUSABLE.getValue().get(this)
								.filter(FunctionalUtilities.not(Collection::isEmpty))
								.map(f -> f.get(Math.floorMod(
										Math.max(f.indexOf(cf), 0) + (next ? 1 : -1), f.size()))));
		if (!ret.isPresent())
			ret = getManager();
		return ret;
	}

	@Override
	public Optional<? extends M> getManager() { return Optional.ofNullable(manager); }

	@Override
	public void setManager(@Nullable M manager) {
		getManager().ifPresent(previousManager -> EventBusUtilities.runWithPrePostHooks(UIEventBusEntryPoint.getEventBus(),
				() -> previousManager.setView(null),
				new EventUIComponentHierarchyChanged.View(EnumEventHookStage.PRE, previousManager, this, null),
				new EventUIComponentHierarchyChanged.View(EnumEventHookStage.POST, previousManager, this, null)));
		this.manager = manager;
		Optional.ofNullable(manager).ifPresent(nextManager -> EventBusUtilities.runWithPrePostHooks(UIEventBusEntryPoint.getEventBus(),
				() -> nextManager.setView(this),
				new EventUIComponentHierarchyChanged.View(EnumEventHookStage.PRE, nextManager, this, null),
				new EventUIComponentHierarchyChanged.View(EnumEventHookStage.POST, nextManager, this, null)));
	}

	@Override
	public IUIComponentPathResolver<IUIComponent> getPathResolver() { return pathResolver; }

	@Override
	public IUIComponentShapeAnchorController getShapeAnchorController() { return shapeAnchorController; }

	@Override
	public IAffineTransformStack getCleanTransformStack() { return new AffineTransformStack(); }

	@Override
	public List<IUIComponent> getChildrenFlatView() {
		return CacheViewComponent.CHILDREN_FLAT.getValue().get(this)
				.orElseThrow(AssertionError::new);
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public Iterable<? extends ObservableSource<IBinderAction>> getBinderNotifiers() {
		return Iterables.concat(getChildrenFlatView().stream().unordered()
						.flatMap(c -> Streams.stream(c.getBinderNotifiers()))
						.collect(ImmutableSet.toImmutableSet()),
				super.getBinderNotifiers());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterable<? extends IBindingField<?>> getBindingFields() {
		return Iterables.concat(Lists.asList(
				super.getBindingFields(),
				(Iterable<IBindingField<?>>[]) // COMMENT should be safe
						getChildrenFlatView().stream().unordered()
								.map(IHasBinding::getBindingFields)
								.toArray(Iterable[]::new)));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterable<? extends IBindingMethod<?>> getBindingMethods() {
		return Iterables.concat(Lists.asList(
				super.getBindingMethods(),
				(Iterable<IBindingMethod<?>>[]) // COMMENT should be safe
						getChildrenFlatView().stream().unordered()
								.map(IHasBinding::getBindingMethods)
								.toArray(Iterable[]::new)));
	}

	@Override
	public boolean reshape(Function<? super IShapeDescriptor<? super S>, ? extends Boolean> action) throws ConcurrentModificationException {
		return getManager()
				.filter(manager -> manager.reshape(action))
				.isPresent();
	}

	@Override
	public void initialize() {
		getManager().ifPresent(manager ->
				IUIViewComponent.traverseComponentTreeDefault(getCleanTransformStack(),
						manager,
						(context, component) -> component.initialize(context),
						IConsumer3.StaticHolder.empty()));
	}

	@Override
	public void removed() {
		getManager().ifPresent(manager ->
				IUIViewComponent.traverseComponentTreeDefault(getCleanTransformStack(),
						manager,
						(context, component) -> component.removed(context),
						IConsumer3.StaticHolder.empty()));
	}

	@Override
	public Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappingsView() { return ImmutableMap.copyOf(getMappings()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappings() { return mappings; }

	public enum CacheViewComponent {
		;

		private static final Logger LOGGER = LogManager.getLogger();

		@SuppressWarnings("UnstableApiUsage")
		public static final Registry.RegistryObject<IUIExtensionCache.IType<List<IUIComponent>, IUIViewComponent<?, ?>>> CHILDREN_FLAT =
				IUIExtensionCache.RegUICache.INSTANCE.registerApply(generateKey("children_flat"),
						k -> new IUIExtensionCache.IType.Impl<>(k,
								(type, instance) -> IUIExtensionCache.TYPE.getValue().get(instance).flatMap(cache -> ThrowableUtilities.Try.call(() -> {
									@SuppressWarnings("unchecked") @Nullable List<WeakReference<IUIComponent>> cv =
											(java.util.List<WeakReference<IUIComponent>>) cache.getDelegated().getIfPresent(type.getKey());
									List<IUIComponent> ret;
									if (cv == null) {
										ret = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_LARGE);
										instance.getManager()
												.ifPresent(manager ->
														TreeUtilities.<IUIComponent, Object>visitNodes(TreeUtilities.EnumStrategy.DEPTH_FIRST, manager,
																ret::add,
																IUIComponent::getChildNodes, null, null));
										cache.getDelegated().put(type.getKey(),
												ret.stream().sequential()
														.map(WeakReference::new)
														.collect(Collectors.toCollection(LinkedList::new)));
									} else {
										cv.removeIf(wr ->
												wr.get() == null);
										ret = cv.stream().sequential()
												.map(Reference::get)
												.filter(Objects::nonNull)
												.collect(ImmutableList.toImmutableList());
									}
									return ret;
								}, LOGGER)),
								(t, i) -> IUIExtensionCache.IType.invalidateImpl(i, t.getKey()),
								type -> ImmutableList.of(
										new DisposableObserverAuto<EventUIComponentHierarchyChanged.Parent>() {
											@Override
											@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
											public void onNext(EventUIComponentHierarchyChanged.Parent event) {
												if (event.getStage().isPost())
													CastUtilities.castChecked(IUIComponentManager.class, event.getComponent())
															.flatMap(IUIComponent::getManager)
															.flatMap(IUIComponentManager::getView)
															.ifPresent(type::invalidate);
											}
										},
										new DisposableObserverAuto<EventUIComponentHierarchyChanged.View>() {
											@Override
											@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
											public void onNext(EventUIComponentHierarchyChanged.View event) {
												if (event.getStage().isPost())
													CastUtilities.castChecked(IUIComponentManager.class, event.getComponent())
															.flatMap(IUIComponent::getManager)
															.flatMap(IUIComponentManager::getView)
															.ifPresent(type::invalidate);
											}
										})));
		@SuppressWarnings("UnstableApiUsage")
		public static final Registry.RegistryObject<IUIExtensionCache.IType<java.util.List<IUIComponent>, IUIViewComponent<?, ?>>> CHILDREN_FLAT_FOCUSABLE =
				IUIExtensionCache.RegUICache.INSTANCE.registerApply(generateKey("children_flat.focusable"),
						k -> new IUIExtensionCache.IType.Impl<>(k,
								(t, i) -> IUIExtensionCache.TYPE.getValue().get(i).flatMap(cache -> ThrowableUtilities.Try.call(() -> {
									@SuppressWarnings("unchecked") @Nullable java.util.List<WeakReference<IUIComponent>> cv =
											(java.util.List<WeakReference<IUIComponent>>) cache.getDelegated().getIfPresent(t.getKey());
									List<IUIComponent> ret;
									if (cv == null) {
										ret = i.getChildrenFlatView().stream().sequential()
												.filter(IUIComponent::isFocusable)
												.collect(ImmutableList.toImmutableList());
										cache.getDelegated().put(t.getKey(),
												ret.stream().sequential()
														.map(WeakReference::new)
														.collect(Collectors.toCollection(LinkedList::new)));
									} else {
										cv.removeIf(wr ->
												wr.get() == null);
										ret = cv.stream().sequential()
												.map(Reference::get)
												.filter(Objects::nonNull)
												.collect(ImmutableList.toImmutableList());
									}
									return ret;
								}, LOGGER)),
								(t, i) -> IUIExtensionCache.IType.invalidateImpl(i, t.getKey()),
								type -> ImmutableList.of(
										new DisposableObserverAuto<EventUIComponentHierarchyChanged.Parent>() {
											@Override
											@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
											public void onNext(EventUIComponentHierarchyChanged.Parent event) {
												if (event.getStage().isPost())
													CastUtilities.castChecked(IUIComponentManager.class, event.getComponent())
															.flatMap(IUIComponent::getManager)
															.flatMap(IUIComponentManager::getView)
															.ifPresent(type::invalidate);
											}
										},
										new DisposableObserverAuto<EventUIComponentHierarchyChanged.View>() {
											@Override
											@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
											public void onNext(EventUIComponentHierarchyChanged.View event) {
												if (event.getStage().isPost())
													CastUtilities.castChecked(IUIComponentManager.class, event.getComponent())
															.flatMap(IUIComponent::getManager)
															.flatMap(IUIComponentManager::getView)
															.ifPresent(type::invalidate);
											}
										})));

		private static INamespacePrefixedString generateKey(@SuppressWarnings("SameParameterValue") String name) { return new NamespacePrefixedString(INamespacePrefixedString.DEFAULT_NAMESPACE, CacheViewComponent.class.getName() + '.' + name); }
	}

	protected class InnerPathResolver extends UIComponentPathResolver {
		protected InnerPathResolver() { super(UIViewComponent.this); }
	}


}
