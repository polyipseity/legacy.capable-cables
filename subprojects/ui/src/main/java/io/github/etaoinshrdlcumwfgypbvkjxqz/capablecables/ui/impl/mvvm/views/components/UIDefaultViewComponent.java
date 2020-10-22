package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components;

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
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.theming.IUIThemeStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.bus.UIEventBusEntryPoint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.UIAbstractView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions.caches.UIAbstractCacheType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions.caches.UIDefaultCacheExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.paths.ArrayAffineTransformStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.paths.UIDefaultComponentPathResolver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.bus.UIAbstractComponentHierarchyChangeBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.theming.UIArrayThemeStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.FunctionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IConsumer3;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.LoggingDisposableObserver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.paths.FunctionalPath;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.core.IRegistryObject;
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

public class UIDefaultViewComponent<S extends Shape, M extends IUIComponentManager<S>>
		extends UIAbstractView<S>
		implements IUIViewComponent<S, M> {
	private final Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings;
	private final IUIComponentPathResolver pathResolver = new UIDefaultComponentPathResolver();
	private final IUIComponentShapeAnchorController shapeAnchorController = new UIDefaultComponentShapeAnchorController();
	private final IUIThemeStack themeStack;
	@Nullable
	private M manager;

	@SuppressWarnings("ThisEscapedInObjectConstruction")
	@UIViewComponentConstructor
	public UIDefaultViewComponent(UIViewComponentConstructor.IArguments arguments) {
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

		IExtensionContainer.addExtensionChecked(this, new UIDefaultCacheExtension());
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
	public Optional<? extends IUIComponentContext> createComponentContext() throws IllegalStateException {
		return getContext()
				.map(context ->
						new UIImmutableComponentContext(
								context,
								this,
								new UIDefaultComponentContextStackMutator(),
								new UIImmutableComponentContextStack(new FunctionalPath<>(ImmutableList.of(), Lists::newArrayList), new ArrayAffineTransformStack(CapacityUtilities.getInitialCapacityMedium()))
						)
				);
	}

	@Override
	public Optional<? extends M> getManager() { return Optional.ofNullable(manager); }

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
	public List<IUIComponent> getChildrenFlatView() {
		return CacheViewComponent.getChildrenFlat().getValue().get(this)
				.orElseThrow(AssertionError::new);
	}

	@Override
	public IUIComponentPathResolver getPathResolver() { return pathResolver; }

	@Override
	public IUIComponentShapeAnchorController getShapeAnchorController() { return shapeAnchorController; }

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
	public boolean reshape(Predicate<? super IShapeDescriptor<? super S>> action) throws ConcurrentModificationException {
		return getManager()
				.filter(manager -> manager.reshape(action))
				.isPresent();
	}

	@Override
	public Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappingsView() { return ImmutableMap.copyOf(getMappings()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappings() { return mappings; }

	@Override
	public void setContext(@Nullable IUIViewContext context) {
		super.setContext(context);
		if (getContext().isPresent())
			getShapeAnchorController().anchor();
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initializeBindings(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		super.initializeBindings(binderObserverSupplier);
		getManager().ifPresent(manager -> manager.initializeBindings(binderObserverSupplier));
	}

	public enum CacheViewComponent {
		;

		@SuppressWarnings({"ThisEscapedInObjectConstruction", "rawtypes", "RedundantSuppression", "unchecked", "AnonymousInnerClassMayBeStatic"})
		private static final IRegistryObject<IUICacheType<List<IUIComponent>, IUIViewComponent<?, ?>>> CHILDREN_FLAT =
				AssertionUtilities.assertNonnull(FunctionUtilities.apply(IUICacheType.generateKey("children_flat"),
						key -> UICacheRegistry.getInstance().register(key,
								new UIAbstractCacheType<List<IUIComponent>, IUIViewComponent<?, ?>>(key) {
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
								})));
		@SuppressWarnings({"UnstableApiUsage", "ThisEscapedInObjectConstruction", "rawtypes", "unchecked", "RedundantSuppression", "AnonymousInnerClassMayBeStatic"})
		private static final IRegistryObject<IUICacheType<List<IUIComponent>, IUIViewComponent<?, ?>>> CHILDREN_FLAT_FOCUSABLE =
				AssertionUtilities.assertNonnull(FunctionUtilities.apply(IUICacheType.generateKey("children_flat.focusable"),
						key -> UICacheRegistry.getInstance().register(key,
								new UIAbstractCacheType<List<IUIComponent>, IUIViewComponent<?, ?>>(key) {
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
										return container.getChildrenFlatView().stream()
												.filter(IUIComponent::isFocusable)
												.collect(ImmutableList.toImmutableList());
									}
								})));

		public static IRegistryObject<IUICacheType<List<IUIComponent>, IUIViewComponent<?, ?>>> getChildrenFlat() {
			return CHILDREN_FLAT;
		}

		public static IRegistryObject<IUICacheType<List<IUIComponent>, IUIViewComponent<?, ?>>> getChildrenFlatFocusable() {
			return CHILDREN_FLAT_FOCUSABLE;
		}
	}
}
