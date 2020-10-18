package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.caches.IUICacheType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.caches.UICacheRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentLifecycleModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.paths.IUIComponentPathResolver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIViewComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.theming.IUIThemeStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.events.bus.UIEventBusEntryPoint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.UIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.extensions.caches.AbstractUICacheType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.extensions.caches.UICacheExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.paths.DefaultUIComponentPathResolver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.events.bus.UIAbstractComponentHierarchyChangeBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.ArrayAffineTransformStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.theming.UIArrayThemeStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CleanerUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.TreeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.FunctionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IConsumer3;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.LoggingDisposableObserver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.registering.Registry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.paths.FunctionalPath;
import io.reactivex.rxjava3.observers.DisposableObserver;
import net.minecraftforge.eventbus.api.EventPriority;
import sun.misc.Cleaner;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class UIViewComponent<S extends Shape, M extends IUIComponentManager<S>>
		extends UIView<S>
		implements IUIViewComponent<S, M> {
	private final Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings;
	private final IUIComponentPathResolver pathResolver = new DefaultUIComponentPathResolver();
	private final IUIComponentShapeAnchorController shapeAnchorController = new DefaultUIComponentShapeAnchorController();
	private final IUIThemeStack themeStack;
	@Nullable
	private M manager;

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initializeBindings(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		super.initializeBindings(binderObserverSupplier);
		getManager().ifPresent(manager -> manager.initializeBindings(binderObserverSupplier));
	}

	@SuppressWarnings("ThisEscapedInObjectConstruction")
	@UIViewComponentConstructor
	public UIViewComponent(UIViewComponentConstructor.IArguments arguments) {
		Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = arguments.getMappingsView();
		this.mappings = MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(mappings.size()).makeMap();
		this.mappings.putAll(mappings);

		this.themeStack = new UIArrayThemeStack(
				theme ->
						theme.apply(
								getNamedTrackers()
										.getTracker(CastUtilities.<Class<IUIRendererContainer<?>>>castUnchecked(IUIRendererContainer.class))
										.asMapView()
										.values()
						),
				CapacityUtilities.getInitialCapacitySmall()
		);
		this.themeStack.push(new UIDefaultViewComponentTheme());

		IExtensionContainer.addExtensionChecked(this, new UICacheExtension());
	}

	@Override
	public IUIThemeStack getThemeStack() { return themeStack; }

	@Override
	public IUIEventTarget getTargetAtPoint(Point2D point) {
		try (IUIComponentContext componentContext = IUIViewComponent.createComponentContextWithManager(this)
				.orElseThrow(IllegalStateException::new)) {
			// COMMENT returning null means the point is outside the window, so in that case, just return the manager
			return getPathResolver().resolvePath(componentContext, (Point2D) point.clone()).getComponent()
					.map(Function.<IUIComponent>identity())
					.orElseGet(() ->
							getManager().orElseThrow(IllegalStateException::new));
		}
	}

	@Override
	public Optional<? extends IUIEventTarget> changeFocus(@Nullable IUIEventTarget currentFocus, boolean next) {
		@Nullable Optional<? extends IUIEventTarget> ret = CastUtilities.castChecked(IUIComponent.class, currentFocus)
				.flatMap(cf ->
						CacheViewComponent.getChildrenFlatFocusable().getValue().get(this)
								.filter(FunctionUtilities.notPredicate(Collection<IUIComponent>::isEmpty))
								.map(f -> f.get(Math.floorMod(
										Math.max(f.indexOf(cf), 0) + (next ? 1 : -1), f.size()))));
		if (!ret.isPresent())
			ret = getManager();
		return ret;
	}

	@Override
	public void setManager(@Nullable M manager) {
		getManager().ifPresent(previousManager -> EventBusUtilities.runWithPrePostHooks(UIEventBusEntryPoint.getEventBus(),
				() -> {
					previousManager.setView(null);
					getNamedTrackers().removeAll(IUIComponent.class, getChildrenFlatView());
				},
				new UIAbstractComponentHierarchyChangeBusEvent.View(EnumHookStage.PRE, previousManager, this, null),
				new UIAbstractComponentHierarchyChangeBusEvent.View(EnumHookStage.POST, previousManager, this, null)));
		this.manager = manager;
		Optional.ofNullable(manager).ifPresent(nextManager -> EventBusUtilities.runWithPrePostHooks(UIEventBusEntryPoint.getEventBus(),
				() -> {
					nextManager.setView(this);
					getBinderObserverSupplier().ifPresent(nextManager::initializeBindings);
					getNamedTrackers().addAll(IUIComponent.class, getChildrenFlatView());
				},
				new UIAbstractComponentHierarchyChangeBusEvent.View(EnumHookStage.PRE, nextManager, null, this),
				new UIAbstractComponentHierarchyChangeBusEvent.View(EnumHookStage.POST, nextManager, null, this)));
	}

	@Override
	public Optional<? extends M> getManager() { return Optional.ofNullable(manager); }

	@SuppressWarnings("RedundantTypeArguments")
	@Override
	public void initialize() {
		getManager().ifPresent(manager ->
				IUIViewComponent.<RuntimeException>traverseComponentTreeDefault(createComponentContext()
								.orElseThrow(IllegalStateException::new),
						manager,
						(componentContext, result) -> {
							assert componentContext != null;
							assert result != null;
							IUIComponentLifecycleModifier.handleComponentModifiers(result.getComponent(),
									result.getModifiersView(),
									componentContext,
									IUIComponentLifecycleModifier::initialize);
						},
						IConsumer3.StaticHolder.getEmpty()));
	}

	@Override
	public IUIComponentShapeAnchorController getShapeAnchorController() { return shapeAnchorController; }

	@Override
	public Optional<? extends IUIComponentContext> createComponentContext() throws IllegalStateException {
		return getContext()
				.map(context ->
						new ImmutableUIComponentContext(
								context,
								this,
								new DefaultUIComponentContextStackMutator(),
								new ImmutableUIComponentContextStack(new FunctionalPath<>(ImmutableList.of(), Lists::newArrayList), new ArrayAffineTransformStack(CapacityUtilities.getInitialCapacityMedium()))
						)
				);
	}

	@Override
	public List<IUIComponent> getChildrenFlatView() {
		return CacheViewComponent.getChildrenFlat().getValue().get(this)
				.orElseThrow(AssertionError::new);
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
	public void removed() {
		getManager().ifPresent(manager ->
				IUIViewComponent.<RuntimeException>traverseComponentTreeDefault(createComponentContext().orElseThrow(IllegalStateException::new),
						manager,
						(componentContext, result) -> {
							assert componentContext != null;
							assert result != null;
							IUIComponentLifecycleModifier.handleComponentModifiers(result.getComponent(),
									result.getModifiersView(),
									componentContext,
									IUIComponentLifecycleModifier::removed);
						},
						IConsumer3.StaticHolder.getEmpty()));
	}

	@Override
	public Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappingsView() { return ImmutableMap.copyOf(getMappings()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappings() { return mappings; }

	public enum CacheViewComponent {
		;

		@SuppressWarnings({"ThisEscapedInObjectConstruction", "rawtypes", "RedundantSuppression", "unchecked", "AnonymousInnerClassMayBeStatic"})
		private static final Registry.RegistryObject<IUICacheType<List<IUIComponent>, IUIViewComponent<?, ?>>> CHILDREN_FLAT =
				UICacheRegistry.getInstance().registerApply(IUICacheType.generateKey("children_flat"),
						key -> new AbstractUICacheType<List<IUIComponent>, IUIViewComponent<?, ?>>(key) {
							{
								OptionalWeakReference<? extends IUICacheType<?, IUIViewComponent<?, ?>>> thisRef =
										new OptionalWeakReference<>(this);
								Cleaner.create(CleanerUtilities.getCleanerReferent(this),
										new AutoSubscribingCompositeDisposable<>(
												UIEventBusEntryPoint.getEventBus(),
												new LoggingDisposableObserver<UIAbstractComponentHierarchyChangeBusEvent.Parent>(
														new FunctionalEventBusDisposableObserver<>(
																new ImmutableSubscribeEvent(EventPriority.LOWEST, true),
																event -> {
																	if (event.getStage().isPost())
																		thisRef.getOptional()
																				.ifPresent(t -> event.getComponent().getManager()
																						.flatMap(IUIComponentManager::getView)
																						.ifPresent(view -> t.invalidate(view)));
																}),
														UIConfiguration.getInstance().getLogger()
												) {},
												new LoggingDisposableObserver<UIAbstractComponentHierarchyChangeBusEvent.View>(
														new FunctionalEventBusDisposableObserver<>(
																new ImmutableSubscribeEvent(EventPriority.LOWEST, true),
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
								List<IUIComponent> ret = new ArrayList<>(CapacityUtilities.getInitialCapacityLarge());
								container.getManager()
										.ifPresent(manager ->
												TreeUtilities.<IUIComponent, Object>visitNodes(TreeUtilities.EnumStrategy.DEPTH_FIRST, manager,
														ret::add,
														IUIComponent::getChildNodes, null, null));
								return ImmutableList.copyOf(ret);
							}
						});
		@SuppressWarnings({"UnstableApiUsage", "ThisEscapedInObjectConstruction", "rawtypes", "unchecked", "RedundantSuppression", "AnonymousInnerClassMayBeStatic"})
		private static final Registry.RegistryObject<IUICacheType<List<IUIComponent>, IUIViewComponent<?, ?>>> CHILDREN_FLAT_FOCUSABLE =
				UICacheRegistry.getInstance().registerApply(IUICacheType.generateKey("children_flat.focusable"),
						key -> new AbstractUICacheType<List<IUIComponent>, IUIViewComponent<?, ?>>(key) {
							{
								OptionalWeakReference<? extends IUICacheType<?, IUIViewComponent<?, ?>>> thisRef =
										new OptionalWeakReference<>(this);
								Cleaner.create(CleanerUtilities.getCleanerReferent(this),
										new AutoSubscribingCompositeDisposable<>(
												UIEventBusEntryPoint.getEventBus(),
												new LoggingDisposableObserver<UIAbstractComponentHierarchyChangeBusEvent.Parent>(
														new FunctionalEventBusDisposableObserver<>(
																new ImmutableSubscribeEvent(EventPriority.LOWEST, true),
																event -> {
																	if (event.getStage().isPost())
																		thisRef.getOptional()
																				.ifPresent(t -> event.getComponent().getManager()
																						.flatMap(IUIComponentManager::getView)
																						.ifPresent(view -> t.invalidate(view)));
																}),
														UIConfiguration.getInstance().getLogger()
												) {},
												new LoggingDisposableObserver<UIAbstractComponentHierarchyChangeBusEvent.View>(
														new FunctionalEventBusDisposableObserver<>(
																new ImmutableSubscribeEvent(EventPriority.LOWEST, true),
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

		public static Registry.RegistryObject<IUICacheType<List<IUIComponent>, IUIViewComponent<?, ?>>> getChildrenFlat() {
			return CHILDREN_FLAT;
		}

		public static Registry.RegistryObject<IUICacheType<List<IUIComponent>, IUIViewComponent<?, ?>>> getChildrenFlatFocusable() {
			return CHILDREN_FLAT_FOCUSABLE;
		}
	}

	@Override
	public void setContext(@Nullable IUIViewContext context) {
		super.setContext(context);
		if (getContext().isPresent())
			getShapeAnchorController().anchor();
	}
}
