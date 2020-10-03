package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components;

import com.google.common.collect.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentShapeAnchorController;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.caches.IUICacheType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.caches.UICacheRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentLifecycleModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIViewComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.paths.IUIComponentPathResolver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.events.bus.UIEventBusEntryPoint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.UIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.extensions.caches.AbstractUICacheType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.extensions.caches.UICacheExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.events.bus.UIComponentHierarchyChangedBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.ArrayAffineTransformStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.DefaultUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.DefaultUIComponentContextMutator;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.paths.DefaultUIComponentPathResolver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CleanerUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.TreeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.methods.IBindingMethod;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.traits.IHasBinding;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.FunctionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IConsumer3;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.LoggingDisposableObserver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.Registry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.paths.FunctionalPath;
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
	private final IUIComponentPathResolver pathResolver = new DefaultUIComponentPathResolver();
	private final IUIComponentShapeAnchorController shapeAnchorController = new DefaultUIComponentShapeAnchorController();
	@Nullable
	private M manager;

	@SuppressWarnings("ThisEscapedInObjectConstruction")
	@UIViewComponentConstructor(type = UIViewComponentConstructor.EnumConstructorType.MAPPINGS)
	public UIViewComponent(Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings) {
		this.mappings = MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(mappings.size()).makeMap();
		this.mappings.putAll(mappings);

		IExtensionContainer.StaticHolder.addExtensionChecked(this, new UICacheExtension());
	}

	@Override
	public IUIEventTarget getTargetAtPoint(IUIViewContext context, Point2D point) {
		try (IUIComponentContext cCtx = IUIViewComponent.StaticHolder.createComponentContextWithManager(this, context).orElseThrow(IllegalStateException::new)) {
			return getPathResolver().resolvePath(cCtx, (Point2D) point.clone()).getComponent().orElseThrow(AssertionError::new);
		}
	}

	@Override
	public Optional<? extends IUIEventTarget> changeFocus(IUIViewContext context, @Nullable IUIEventTarget currentFocus, boolean next) {
		@Nullable Optional<? extends IUIEventTarget> ret = CastUtilities.castChecked(IUIComponent.class, currentFocus)
				.flatMap(cf ->
						CacheViewComponent.CHILDREN_FLAT_FOCUSABLE.getValue().get(this)
								.filter(FunctionUtilities.notPredicate(Collection<IUIComponent>::isEmpty))
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

	@SuppressWarnings("RedundantTypeArguments")
	@Override
	public void initialize(IUIViewContext context) {
		getManager().ifPresent(manager ->
				IUIViewComponent.StaticHolder.<RuntimeException>traverseComponentTreeDefault(createComponentContext(context),
						manager,
						(componentContext, result) ->
								IUIComponentLifecycleModifier.StaticHolder.handleComponentModifiers(result.getComponent(),
										result.getModifiersView(),
										componentContext,
										IUIComponentLifecycleModifier::initialize),
						IConsumer3.StaticHolder.getEmpty()));
	}

	@Override
	public IUIComponentShapeAnchorController getShapeAnchorController() { return shapeAnchorController; }

	@Override
	public IUIComponentContext createComponentContext(IUIViewContext viewContext) {
		return new DefaultUIComponentContext(
				this,
				new DefaultUIComponentContextMutator(),
				new FunctionalPath<>(ImmutableList.of(), Lists::newArrayList),
				new ArrayAffineTransformStack(),
				viewContext
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

	@Override
	public IUIComponentPathResolver getPathResolver() { return pathResolver; }

	@SuppressWarnings("RedundantTypeArguments")
	@Override
	public void removed(IUIViewContext context) {
		getManager().ifPresent(manager ->
				IUIViewComponent.StaticHolder.<RuntimeException>traverseComponentTreeDefault(createComponentContext(context),
						manager,
						(componentContext, result) ->
								IUIComponentLifecycleModifier.StaticHolder.handleComponentModifiers(result.getComponent(),
										result.getModifiersView(),
										componentContext,
										IUIComponentLifecycleModifier::removed),
						IConsumer3.StaticHolder.getEmpty()));
	}

	@Override
	public Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappingsView() { return ImmutableMap.copyOf(getMappings()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappings() { return mappings; }

	public enum CacheViewComponent {
		;

		@SuppressWarnings({"ThisEscapedInObjectConstruction", "rawtypes", "RedundantSuppression", "unchecked", "AnonymousInnerClassMayBeStatic"})
		public static final Registry.RegistryObject<IUICacheType<List<IUIComponent>, IUIViewComponent<?, ?>>> CHILDREN_FLAT =
				UICacheRegistry.getInstance().registerApply(generateKey("children_flat"),
						key -> new AbstractUICacheType<List<IUIComponent>, IUIViewComponent<?, ?>>(key) {
							{
								OptionalWeakReference<? extends IUICacheType<?, IUIViewComponent<?, ?>>> thisRef =
										new OptionalWeakReference<>(this);
								Cleaner.create(CleanerUtilities.getCleanerReferent(this),
										new AutoSubscribingCompositeDisposable<>(
												UIEventBusEntryPoint.getEventBus(),
												new LoggingDisposableObserver<UIComponentHierarchyChangedBusEvent.Parent>(
														new FunctionalEventBusDisposableObserver<>(
																new SubscribeEventObject(EventPriority.LOWEST, true),
																event -> {
																	if (event.getStage().isPost())
																		thisRef.getOptional()
																				.ifPresent(t -> event.getComponent().getManager()
																						.flatMap(IUIComponentManager::getView)
																						.ifPresent(view -> t.invalidate(view)));
																}),
														UIConfiguration.getInstance().getLogger()
												) {},
												new LoggingDisposableObserver<UIComponentHierarchyChangedBusEvent.View>(
														new FunctionalEventBusDisposableObserver<>(
																new SubscribeEventObject(EventPriority.LOWEST, true),
																event -> {
																	if (event.getStage().isPost())
																		thisRef.getOptional()
																				.ifPresent(t -> event.getComponent().getManager()
																						.flatMap(IUIComponentManager::getView)
																						.ifPresent(view -> t.invalidate(view)));
																}),
														UIConfiguration.getInstance().getLogger()
												) {}
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
		@SuppressWarnings({"UnstableApiUsage", "ThisEscapedInObjectConstruction", "rawtypes", "unchecked", "RedundantSuppression", "AnonymousInnerClassMayBeStatic"})
		public static final Registry.RegistryObject<IUICacheType<List<IUIComponent>, IUIViewComponent<?, ?>>> CHILDREN_FLAT_FOCUSABLE =
				UICacheRegistry.getInstance().registerApply(generateKey("children_flat.focusable"),
						key -> new AbstractUICacheType<List<IUIComponent>, IUIViewComponent<?, ?>>(key) {
							{
								OptionalWeakReference<? extends IUICacheType<?, IUIViewComponent<?, ?>>> thisRef =
										new OptionalWeakReference<>(this);
								Cleaner.create(CleanerUtilities.getCleanerReferent(this),
										new AutoSubscribingCompositeDisposable<>(
												UIEventBusEntryPoint.getEventBus(),
												new LoggingDisposableObserver<UIComponentHierarchyChangedBusEvent.Parent>(
														new FunctionalEventBusDisposableObserver<>(
																new SubscribeEventObject(EventPriority.LOWEST, true),
																event -> {
																	if (event.getStage().isPost())
																		thisRef.getOptional()
																				.ifPresent(t -> event.getComponent().getManager()
																						.flatMap(IUIComponentManager::getView)
																						.ifPresent(view -> t.invalidate(view)));
																}),
														UIConfiguration.getInstance().getLogger()
												) {},
												new LoggingDisposableObserver<UIComponentHierarchyChangedBusEvent.View>(
														new FunctionalEventBusDisposableObserver<>(
																new SubscribeEventObject(EventPriority.LOWEST, true),
																event -> {
																	if (event.getStage().isPost())
																		thisRef.getOptional()
																				.ifPresent(t -> event.getComponent().getManager()
																						.flatMap(IUIComponentManager::getView)
																						.ifPresent(view -> t.invalidate(view)));
																}),
														UIConfiguration.getInstance().getLogger()
												) {}
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
