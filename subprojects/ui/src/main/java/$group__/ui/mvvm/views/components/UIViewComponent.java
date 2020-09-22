package $group__.ui.mvvm.views.components;

import $group__.ui.UIConfiguration;
import $group__.ui.core.binding.IUIPropertyMappingValue;
import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.IUIComponentManager;
import $group__.ui.core.mvvm.views.components.IUIComponentShapeAnchorController;
import $group__.ui.core.mvvm.views.components.IUIViewComponent;
import $group__.ui.core.mvvm.views.components.extensions.caches.IUICacheType;
import $group__.ui.core.mvvm.views.components.extensions.caches.UICacheRegistry;
import $group__.ui.core.mvvm.views.events.IUIEventTarget;
import $group__.ui.core.parsers.components.UIViewComponentConstructor;
import $group__.ui.core.structures.IUIComponentContext;
import $group__.ui.core.structures.paths.IUIComponentPathResolver;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.ui.events.bus.UIEventBusEntryPoint;
import $group__.ui.mvvm.views.UIView;
import $group__.ui.mvvm.views.components.extensions.caches.AbstractUICacheType;
import $group__.ui.mvvm.views.components.extensions.caches.UICacheExtension;
import $group__.ui.mvvm.views.events.bus.UIComponentHierarchyChangedBusEvent;
import $group__.ui.structures.ArrayAffineTransformStack;
import $group__.ui.structures.DefaultUIComponentContext;
import $group__.ui.structures.paths.DefaultUIComponentPathResolver;
import $group__.utilities.CapacityUtilities;
import $group__.utilities.CastUtilities;
import $group__.utilities.CleanerUtilities;
import $group__.utilities.TreeUtilities;
import $group__.utilities.binding.core.IBinderAction;
import $group__.utilities.binding.core.fields.IBindingField;
import $group__.utilities.binding.core.methods.IBindingMethod;
import $group__.utilities.binding.core.traits.IHasBinding;
import $group__.utilities.collections.MapUtilities;
import $group__.utilities.events.*;
import $group__.utilities.extensions.core.IExtensionContainer;
import $group__.utilities.functions.FunctionUtilities;
import $group__.utilities.functions.IConsumer3;
import $group__.utilities.minecraft.client.GLUtilities;
import $group__.utilities.reactive.LoggingDisposableObserver;
import $group__.utilities.references.OptionalWeakReference;
import $group__.utilities.structures.INamespacePrefixedString;
import $group__.utilities.structures.ImmutableNamespacePrefixedString;
import $group__.utilities.structures.Registry;
import $group__.utilities.structures.paths.FunctionalPath;
import com.google.common.collect.*;
import io.reactivex.rxjava3.core.ObservableSource;
import net.minecraftforge.eventbus.api.EventPriority;
import org.jetbrains.annotations.NonNls;
import sun.misc.Cleaner;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.*;
import java.util.function.Predicate;

public class UIViewComponent<S extends Shape, M extends IUIComponentManager<S>>
		extends UIView<S>
		implements IUIViewComponent<S, M> {
	private final Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings;
	private final IUIComponentPathResolver<IUIComponent> pathResolver = new DefaultUIComponentPathResolver();
	private final IUIComponentShapeAnchorController shapeAnchorController = new DefaultUIComponentShapeAnchorController();
	@Nullable
	private M manager;

	@SuppressWarnings("ThisEscapedInObjectConstruction")
	@UIViewComponentConstructor(type = UIViewComponentConstructor.EnumConstructorType.MAPPINGS)
	public UIViewComponent(Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings) {
		this.mappings = MapUtilities.newMapMakerSingleThreaded().initialCapacity(mappings.size()).makeMap();
		this.mappings.putAll(mappings);

		IExtensionContainer.StaticHolder.addExtensionChecked(this, new UICacheExtension());
	}

	@Override
	public IUIEventTarget getTargetAtPoint(Point2D point) {
		try (IUIComponentContext context = createContext()) {
			getPathResolver().resolvePath(context, point, true);
			return context.getPath().getPathEnd();
		}
	}

	@Override
	public Optional<? extends IUIEventTarget> changeFocus(@Nullable IUIEventTarget currentFocus, boolean next) {
		@Nullable Optional<? extends IUIEventTarget> ret = CastUtilities.castChecked(IUIComponent.class, currentFocus)
				.flatMap(cf ->
						CacheViewComponent.CHILDREN_FLAT_FOCUSABLE.getValue().get(this)
								.filter(FunctionUtilities.notPredicate(Collection::isEmpty))
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
				new UIComponentHierarchyChangedBusEvent.View(EnumHookStage.PRE, previousManager, this, null),
				new UIComponentHierarchyChangedBusEvent.View(EnumHookStage.POST, previousManager, this, null)));
		this.manager = manager;
		Optional.ofNullable(manager).ifPresent(nextManager -> EventBusUtilities.runWithPrePostHooks(UIEventBusEntryPoint.getEventBus(),
				() -> nextManager.setView(this),
				new UIComponentHierarchyChangedBusEvent.View(EnumHookStage.PRE, nextManager, this, null),
				new UIComponentHierarchyChangedBusEvent.View(EnumHookStage.POST, nextManager, this, null)));
	}

	@Override
	public IUIComponentPathResolver<IUIComponent> getPathResolver() { return pathResolver; }

	@Override
	public IUIComponentShapeAnchorController getShapeAnchorController() { return shapeAnchorController; }

	@Override
	public IUIComponentContext createContext() {
		return new DefaultUIComponentContext(
				new FunctionalPath<>(ImmutableList.of(getManager()
						.orElseThrow(IllegalStateException::new)), Lists::newArrayList),
				new ArrayAffineTransformStack(),
				GLUtilities.getCursorPos()
		);
	}

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
	public boolean reshape(Predicate<? super IShapeDescriptor<? super S>> action) throws ConcurrentModificationException {
		return getManager()
				.filter(manager -> manager.reshape(action))
				.isPresent();
	}

	@SuppressWarnings("RedundantTypeArguments")
	@Override
	public void initialize() {
		getManager().ifPresent(manager ->
				IUIViewComponent.StaticHolder.<RuntimeException>traverseComponentTreeDefault(createContext(),
						manager,
						(context, component) -> component.initialize(context),
						IConsumer3.StaticHolder.empty()));
	}

	@SuppressWarnings("RedundantTypeArguments")
	@Override
	public void removed() {
		getManager().ifPresent(manager ->
				IUIViewComponent.StaticHolder.<RuntimeException>traverseComponentTreeDefault(createContext(),
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

		@SuppressWarnings("ThisEscapedInObjectConstruction")
		public static final Registry.RegistryObject<IUICacheType<List<IUIComponent>, IUIViewComponent<?, ?>>> CHILDREN_FLAT =
				UICacheRegistry.getInstance().registerApply(generateKey("children_flat"),
						key -> new AbstractUICacheType<List<IUIComponent>, IUIViewComponent<?, ?>>(key) {
							{
								OptionalWeakReference<? extends IUICacheType<?, IUIViewComponent<?, ?>>> thisRef =
										new OptionalWeakReference<>(this);
								Cleaner.create(CleanerUtilities.getCleanerReferent(this),
										new AutoSubscribingCompositeDisposable<>(
												UIEventBusEntryPoint.getEventBus(),
												new LoggingDisposableObserver<>(
														new FunctionalEventBusDisposableObserver<UIComponentHierarchyChangedBusEvent.Parent>(
																new SubscribeEventObject(EventPriority.LOWEST, true),
																event -> {
																	if (event.getStage().isPost())
																		thisRef.getOptional()
																				.ifPresent(t -> event.getComponent().getManager()
																						.flatMap(IUIComponentManager::getView)
																						.ifPresent(view -> t.invalidate(view)));
																}),
														UIConfiguration.getInstance().getLogger()
												),
												new LoggingDisposableObserver<>(
														new FunctionalEventBusDisposableObserver<UIComponentHierarchyChangedBusEvent.View>(
																new SubscribeEventObject(EventPriority.LOWEST, true),
																event -> {
																	if (event.getStage().isPost())
																		thisRef.getOptional()
																				.ifPresent(t -> event.getComponent().getManager()
																						.flatMap(IUIComponentManager::getView)
																						.ifPresent(view -> t.invalidate(view)));
																}),
														UIConfiguration.getInstance().getLogger()
												)
										)::dispose);
							}

							@Override
							protected List<IUIComponent> load(IUIViewComponent<?, ?> container) {
								List<IUIComponent> ret = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_LARGE);
								container.getManager()
										.ifPresent(manager ->
												TreeUtilities.<IUIComponent, Object>visitNodes(TreeUtilities.EnumStrategy.DEPTH_FIRST, manager,
														ret::add,
														IUIComponent::getChildNodes, null, null));
								return ImmutableList.copyOf(ret);
							}
						});
		@SuppressWarnings({"UnstableApiUsage", "ThisEscapedInObjectConstruction"})
		public static final Registry.RegistryObject<IUICacheType<List<IUIComponent>, IUIViewComponent<?, ?>>> CHILDREN_FLAT_FOCUSABLE =
				UICacheRegistry.getInstance().registerApply(generateKey("children_flat.focusable"),
						key -> new AbstractUICacheType<List<IUIComponent>, IUIViewComponent<?, ?>>(key) {
							{
								OptionalWeakReference<? extends IUICacheType<?, IUIViewComponent<?, ?>>> thisRef =
										new OptionalWeakReference<>(this);
								Cleaner.create(CleanerUtilities.getCleanerReferent(this),
										new AutoSubscribingCompositeDisposable<>(
												UIEventBusEntryPoint.getEventBus(),
												new LoggingDisposableObserver<>(
														new FunctionalEventBusDisposableObserver<UIComponentHierarchyChangedBusEvent.Parent>(
																new SubscribeEventObject(EventPriority.LOWEST, true),
																event -> {
																	if (event.getStage().isPost())
																		thisRef.getOptional()
																				.ifPresent(t -> event.getComponent().getManager()
																						.flatMap(IUIComponentManager::getView)
																						.ifPresent(view -> t.invalidate(view)));
																}),
														UIConfiguration.getInstance().getLogger()
												),
												new LoggingDisposableObserver<>(
														new FunctionalEventBusDisposableObserver<UIComponentHierarchyChangedBusEvent.View>(
																new SubscribeEventObject(EventPriority.LOWEST, true),
																event -> {
																	if (event.getStage().isPost())
																		thisRef.getOptional()
																				.ifPresent(t -> event.getComponent().getManager()
																						.flatMap(IUIComponentManager::getView)
																						.ifPresent(view -> t.invalidate(view)));
																}),
														UIConfiguration.getInstance().getLogger()
												)
										)::dispose);
							}

							@Override
							protected List<IUIComponent> load(IUIViewComponent<?, ?> container) {
								return container.getChildrenFlatView().stream().sequential()
										.filter(IUIComponent::isFocusable)
										.collect(ImmutableList.toImmutableList());
							}
						});

		private static INamespacePrefixedString generateKey(@SuppressWarnings("SameParameterValue") @NonNls String name) { return new ImmutableNamespacePrefixedString(INamespacePrefixedString.StaticHolder.DEFAULT_NAMESPACE, CacheViewComponent.class.getName() + '.' + name); /* TODO extract this method */ }
	}
}
