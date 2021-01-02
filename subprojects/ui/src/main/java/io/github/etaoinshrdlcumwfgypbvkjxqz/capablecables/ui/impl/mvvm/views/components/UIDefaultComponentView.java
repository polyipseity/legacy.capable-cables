package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.AlwaysNull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction.IUIViewArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction.UIViewConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.IUIStructureLifecycleContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.lifecycles.EnumUILifecycleState;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.lifecycles.IUIStructureLifecycle;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.IUIViewCoordinator;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.extensions.caches.IUICacheType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.extensions.caches.UICacheRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.modifiers.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.paths.IUIComponentPathResolver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.events.IUIEventTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.rendering.IUIComponentRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.rendering.IUIComponentRendererInvokerModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.shapes.interactions.IShapeDescriptorDynamicDetector;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.theming.IUIThemeStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.bus.UIEventBusEntryPoint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.UIAbstractView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions.UIComponentCursorHandleProviderExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions.caches.UIAbstractCacheType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions.caches.UIDefaultCacheExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.paths.UIDefaultComponentPathResolver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.bus.UIAbstractComponentHierarchyChangeBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.interactions.UIFunctionalComponentShapeDescriptorDynamicDetector;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.theming.UIArrayThemeStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.theming.UIDefaultingTheme;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.CollectionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.def.IConsumer3;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.impl.FunctionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.impl.OneUseConsumer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.impl.OneUseRunnable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.DelegatingSubscriber;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.FunctionalNextSubscriber;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.ReactiveUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.def.IExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.def.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.def.IRegistryObject;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import sun.misc.Cleaner;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressThisEscapedWarning;

public class UIDefaultComponentView<S extends Shape, M extends IUIComponentManager<S>>
		extends UIAbstractView<S>
		implements IUIComponentView<S, M> {
	private final Map<IIdentifier, IUIPropertyMappingValue> mappings;
	private final Consumer<ConcurrentMap<Class<?>, IUIViewCoordinator>> coordinatorMapInitializer;
	private final Runnable extensionsInitializer;
	@SuppressWarnings("ThisEscapedInObjectConstruction")
	private final AutoCloseableRotator<DisposableSubscriber<UIAbstractComponentHierarchyChangeBusEvent.Parent>, RuntimeException> componentHierarchyChangeParentSubscriberRotator =
			new AutoCloseableRotator<>(() -> ComponentHierarchyChangeParentSubscriber.ofDecorated(this, UIConfiguration.getInstance().getLogger()), Disposable::dispose);

	private @Nullable M internalManager;

	@SuppressWarnings("RedundantTypeArguments")
	@UIViewConstructor
	protected UIDefaultComponentView(IUIViewArguments arguments) {
		Map<IIdentifier, IUIPropertyMappingValue> mappings = arguments.getMappingsView();
		this.mappings = MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(mappings.size()).makeMap();
		this.mappings.putAll(mappings);

		OptionalWeakReference<UIDefaultComponentView<S, M>> thisReference = OptionalWeakReference.of(suppressThisEscapedWarning(() -> this));
		this.coordinatorMapInitializer = new OneUseConsumer<>(field -> {
			IUIThemeStack themeStack = new UIArrayThemeStack(
					theme ->
							theme.apply(
									IUIView.getNamedTrackers(this)
											.getTracker(CastUtilities.<Class<IUIRendererContainer<?>>>castUnchecked(IUIRendererContainer.class))
											.asMapView()
											.values()
											.iterator()
							),
					CapacityUtilities.getInitialCapacitySmall()
			);
			themeStack.push(new UIDefaultingTheme());

			field.put(IUIComponentPathResolver.class, new UIDefaultComponentPathResolver());
			field.put(IUIComponentShapeAnchorController.class, new UIDefaultComponentShapeAnchorController());
			field.put(IUIThemeStack.class, themeStack);
			field.put(IShapeDescriptorDynamicDetector.class, new UIFunctionalComponentShapeDescriptorDynamicDetector(
					() -> thisReference.getOptional()
							.map(UIDefaultComponentView<S, M>::getChildrenFlatView)
							.map(Iterable::iterator)
							.orElseGet(CollectionUtilities::getEmptyIterator)));
		});
		this.extensionsInitializer = new OneUseRunnable(() -> {
			IExtensionContainer.addExtensionChecked(this, new UIDefaultCacheExtension());
			IExtensionContainer.addExtensionChecked(this, new UIComponentCursorHandleProviderExtension()); // COMMENT delegate cursor support to components
		});
	}

	@Override
	public @Immutable List<? extends IUIComponent> getChildrenFlatView() {
		return ComponentViewCache.getChildrenFlat().getValue().get(this)
				.orElseThrow(AssertionError::new);
	}

	public static <S extends Shape, M extends IUIComponentManager<S>> UIDefaultComponentView<S, M> of(IUIViewArguments arguments,
	                                                                                                  M manager) {
		return IUIComponentView.create(() -> new UIDefaultComponentView<>(arguments),
				manager);
	}

	@Override
	public Map<IIdentifier, IUIPropertyMappingValue> getMappingsView() { return ImmutableMap.copyOf(getMappings()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Map<IIdentifier, IUIPropertyMappingValue> getMappings() { return mappings; }

	@Override
	protected ConcurrentMap<IIdentifier, IExtension<? extends IIdentifier, ?>> getExtensions() {
		extensionsInitializer.run();
		return super.getExtensions();
	}

	@Override
	public void setContext(@Nullable IUIViewContext context) {
		super.setContext(context);
		if (getContext().isPresent())
			IUIComponentView.getShapeAnchorController(this).anchor();
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	protected void unbind0(@SuppressWarnings("unused") @AlwaysNull @Nullable Void context) {
		IUIComponentView.traverseComponentTreeDefault(getManager(),
				component ->
						IUIComponentStructureLifecycleModifier.handleComponentModifiers(component,
								component.getModifiersView(),
								IUIStructureLifecycle::unbindV),
				FunctionUtilities.getEmptyBiConsumer());
		getComponentHierarchyChangeParentSubscriberRotator().close();
		super.unbind0(context);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	protected void bind0(IUIStructureLifecycleContext context) {
		super.bind0(context);
		UIEventBusEntryPoint.<UIAbstractComponentHierarchyChangeBusEvent.Parent>getBusPublisher()
				.subscribe(getComponentHierarchyChangeParentSubscriberRotator().get());
		IUIComponentView.traverseComponentTreeDefault(getManager(),
				component ->
						IUIComponentStructureLifecycleModifier.handleComponentModifiers(component,
								component.getModifiersView(),
								modifier -> modifier.bind(context)),
				FunctionUtilities.getEmptyBiConsumer());
	}

	@SuppressWarnings("RedundantTypeArguments")
	@Override
	@OverridingMethodsMustInvokeSuper
	protected void cleanup0(@AlwaysNull @Nullable Void context) {
		try (IUIComponentContext componentContext = createComponentContext().orElseThrow(IllegalStateException::new)) {
			IUIComponentView.<RuntimeException>traverseComponentTreeDefault(componentContext, // TODO javac bug, need explicit type arguments
					getManager(),
					(componentContext1, result) ->
							IUIComponentActiveLifecycleModifier.handleComponentModifiers(result.getComponent(),
									result.getModifiersView(),
									modifier -> modifier.cleanup(componentContext1)),
					IConsumer3.StaticHolder.getEmpty());
		}
		super.cleanup0(context);
	}

	@Override
	public M getManager() {
		return getInternalManager().orElseThrow(IllegalStateException::new);
	}

	protected AutoCloseableRotator<? extends DisposableSubscriber<? super UIAbstractComponentHierarchyChangeBusEvent.Parent>, RuntimeException> getComponentHierarchyChangeParentSubscriberRotator() {
		return componentHierarchyChangeParentSubscriberRotator;
	}

	protected Optional<? extends M> getInternalManager() {
		return Optional.ofNullable(internalManager);
	}

	protected void setInternalManager(M internalManager) {
		this.internalManager = internalManager;
	}

	@SuppressWarnings({"Convert2MethodRef", "rawtypes", "RedundantSuppression"})
	@Override
	public void setManager(M manager) {
		IUIStructureLifecycle.checkBoundState(getLifecycleStateTracker().containsState(EnumUILifecycleState.BOUND), false);
		// COMMENT initially 'null'
		getInternalManager().ifPresent(previousManager -> EventBusUtilities.runWithPrePostHooks(UIEventBusEntryPoint.getBusSubscriber()::onNext,
				() -> previousManager.setView(null),
				new UIAbstractComponentHierarchyChangeBusEvent.View(EnumHookStage.PRE, previousManager, this, null),
				new UIAbstractComponentHierarchyChangeBusEvent.View(EnumHookStage.POST, previousManager, this, null)));
		setInternalManager(manager);
		EventBusUtilities.runWithPrePostHooks(UIEventBusEntryPoint.getBusSubscriber()::onNext,
				() -> getManager().setView(this),
				new UIAbstractComponentHierarchyChangeBusEvent.View(EnumHookStage.PRE, getManager(), null, this),
				new UIAbstractComponentHierarchyChangeBusEvent.View(EnumHookStage.POST, getManager(), null, this));
	}

	@SuppressWarnings("RedundantTypeArguments")
	@Override
	protected void initialize0(@AlwaysNull @Nullable Void context) {
		super.initialize0(context);
		try (IUIComponentContext componentContext = createComponentContext().orElseThrow(IllegalStateException::new)) {
			IUIComponentView.<RuntimeException>traverseComponentTreeDefault(componentContext, // TODO javac bug, need explicit type arguments
					getManager(),
					(componentContext1, result) ->
							IUIComponentActiveLifecycleModifier.handleComponentModifiers(result.getComponent(),
									result.getModifiersView(),
									modifier -> modifier.initialize(componentContext1)),
					IConsumer3.StaticHolder.getEmpty());
		}
	}

	@Override
	public IUIEventTarget getTargetAtPoint(Point2D point) {
		try (IUIComponentContext componentContext = IUIComponentView.createComponentContextWithManager(this)
				.orElseThrow(IllegalStateException::new)) {
			// COMMENT returning null means the point is outside the window, so in that case, just return the manager
			return IUIComponentView.getPathResolver(this)
					.resolvePath(componentContext, (Point2D) point.clone())
					.getComponent()
					.<IUIComponent>map(Function.identity())
					.orElseGet(this::getManager);
		}
	}

	@Override
	@SuppressWarnings({"rawtypes", "RedundantSuppression"})
	public Optional<? extends IUIEventTarget> changeFocus(@Nullable IUIEventTarget currentFocus, boolean next) {
		@Nullable Optional<? extends IUIEventTarget> ret = CastUtilities.castChecked(IUIComponent.class, currentFocus)
				.flatMap(cf ->
						ComponentViewCache.getChildrenFlatFocusable().getValue().get(this)
								.filter(FunctionUtilities.notPredicate(Collection::isEmpty))
								.map(f -> f.get(Math.floorMod(
										Math.max(f.indexOf(cf), 0) + (next ? 1 : -1), f.size()))));
		if (!ret.isPresent())
			ret = Optional.of(getManager());
		return ret;
	}

	@Override
	public boolean reshape(Predicate<@Nonnull ? super IShapeDescriptor<? super S>> action) throws ConcurrentModificationException {
		return getManager().reshape(action);
	}

	@Override
	protected void render0() {
		super.render0();

		IUIComponentView.getShapeDescriptorDynamicDetector(this).detect(); // TODO not good

		try (IUIComponentContext componentContext = createComponentContext()
				.orElseThrow(IllegalStateException::new)) {
			IUIComponentView.traverseComponentTreeDefault(componentContext,
					getManager(),
					(componentContext2, result) -> {
						assert result != null;
						assert componentContext2 != null;

						// COMMENT update the same rate as render, only need to call it before rendering
						IUIComponentUpdateModifier.handleComponentModifiers(result.getComponent(),
								result.getModifiersView(),
								componentContext2);

						IUIComponentModifier.streamSpecificModifiersIntersection(result.getModifiersView().iterator(), IUIComponentRendererInvokerModifier.class)
								.forEachOrdered(modifierIntersection -> {
									IUIComponentModifier left = modifierIntersection.getLeft();
									IUIComponentRendererInvokerModifier right = modifierIntersection.getRight();
									EnumModifyStage.PRE.advanceModifyStage(left);
									right.invokeRenderer(componentContext2); // COMMENT pre
									left.resetModifyStage();
								});
						result.getComponent().getRendererContainer().getRenderer()
								.ifPresent(renderer ->
										renderer.render(componentContext2, IUIComponentRenderer.EnumRenderStage.PRE_CHILDREN
										)
								);
					},
					(componentContext2, result, children) -> {
						assert result != null;
						assert componentContext2 != null;
						result.getComponent().getRendererContainer().getRenderer().ifPresent(renderer ->
								renderer.render(componentContext2, IUIComponentRenderer.EnumRenderStage.POST_CHILDREN
								)
						);
						IUIComponentModifier.streamSpecificModifiersIntersection(result.getModifiersView().iterator(), IUIComponentRendererInvokerModifier.class)
								.forEachOrdered(modifierIntersection -> {
									IUIComponentModifier left = modifierIntersection.getLeft();
									IUIComponentRendererInvokerModifier right = modifierIntersection.getRight();
									EnumModifyStage.POST.advanceModifyStage(left);
									right.invokeRenderer(componentContext2); // COMMENT post
									left.resetModifyStage();
								});
					},
					IUIComponent::isVisible);
		}
	}

	@Override
	protected ConcurrentMap<Class<?>, IUIViewCoordinator> getCoordinatorMap() {
		return FunctionUtilities.accept(super.getCoordinatorMap(), coordinatorMapInitializer);
	}

	@Override
	public Optional<? extends IUIComponentContext> createComponentContext() {
		return getContext()
				.map(context -> new UIDefaultComponentContext(
						context,
						this,
						new UIDefaultComponentContextMutator()
				));
	}

	public enum ComponentViewCache {
		;

		@SuppressWarnings({"AnonymousInnerClass", "rawtypes", "RedundantSuppression", "AnonymousInnerClassMayBeStatic"})
		private static final IRegistryObject<IUICacheType<List<? extends IUIComponent>, IUIComponentView<?, ?>>> CHILDREN_FLAT =
				FunctionUtilities.apply(IUICacheType.generateKey("children_flat"),
						key -> UICacheRegistry.getInstance().register(key,
								new UIAbstractCacheType<List<? extends OptionalWeakReference<? extends IUIComponent>>, List<? extends IUIComponent>, IUIComponentView<?, ?>>(key) {
									{
										OptionalWeakReference<? extends IUICacheType<?, IUIComponentView<?, ?>>> thisRef =
												OptionalWeakReference.of(suppressThisEscapedWarning(() -> this));
										Cleaner.create(suppressThisEscapedWarning(() -> this),
												AutoSubscribingDisposable.of(
														UIEventBusEntryPoint.getBusPublisher(),
														ImmutableList.of(
																new EventBusSubscriber<UIAbstractComponentHierarchyChangeBusEvent.Parent>(
																		ImmutableSubscribeEvent.of(EventPriority.LOWEST, true),
																		ReactiveUtilities.decorateAsListener(
																				delegate -> FunctionalNextSubscriber.of(delegate,
																						event -> {
																							if (event.getStage() == EnumHookStage.POST)
																								thisRef.getOptional()
																										.ifPresent(t -> event.getComponent().getManager()
																												.flatMap(IUIComponentManager::getView)
																												.ifPresent(view -> t.invalidate(view)));
																						}
																				),
																				UIConfiguration.getInstance().getLogger()
																		)
																) {
																	@Override
																	public void onNext(UIAbstractComponentHierarchyChangeBusEvent.Parent event) {
																		onNextImpl(event);
																	}
																},
																new EventBusSubscriber<UIAbstractComponentHierarchyChangeBusEvent.View>(
																		ImmutableSubscribeEvent.of(EventPriority.LOWEST, true),
																		ReactiveUtilities.decorateAsListener(
																				delegate -> FunctionalNextSubscriber.of(delegate,
																						event -> {
																							if (event.getStage() == EnumHookStage.POST)
																								thisRef.getOptional()
																										.ifPresent(t -> event.getComponent().getManager()
																												.flatMap(IUIComponentManager::getView)
																												.ifPresent(view -> t.invalidate(view)));
																						}
																				),
																				UIConfiguration.getInstance().getLogger()
																		)
																) {
																	@Override
																	public void onNext(UIAbstractComponentHierarchyChangeBusEvent.View event) {
																		onNextImpl(event);
																	}
																}
														)
												)::dispose);
									}

									@Override
									protected List<? extends OptionalWeakReference<? extends IUIComponent>> load(IUIComponentView<?, ?> container) {
										List<OptionalWeakReference<IUIComponent>> ret = new ArrayList<>(CapacityUtilities.getInitialCapacityLarge());
										TreeUtilities.<IUIComponent, Void>visitNodes(TreeUtilities.EnumStrategy.DEPTH_FIRST, container.getManager(),
												component -> {
													ret.add(OptionalWeakReference.of(component));
													return null;
												},
												IUIComponent::getChildrenView, null, null);
										return ret; // COMMENT mutable
									}

									@Override
									protected Optional<? extends List<? extends IUIComponent>> transform(IUIComponentView<?, ?> container,
									                                                                     @Nullable List<? extends OptionalWeakReference<? extends IUIComponent>> value) {
										assert value != null;
										return Optional.of(CollectionUtilities.collectOrRemoveReferences(value));
									}
								}));
		@SuppressWarnings({"AnonymousInnerClass", "rawtypes", "RedundantSuppression", "AnonymousInnerClassMayBeStatic"})
		private static final IRegistryObject<IUICacheType<List<? extends IUIComponent>, IUIComponentView<?, ?>>> CHILDREN_FLAT_FOCUSABLE =
				AssertionUtilities.assertNonnull(FunctionUtilities.apply(IUICacheType.generateKey("children_flat.focusable"),
						key -> UICacheRegistry.getInstance().register(key,
								new UIAbstractCacheType<List<? extends OptionalWeakReference<? extends IUIComponent>>, List<? extends IUIComponent>, IUIComponentView<?, ?>>(key) {
									{
										OptionalWeakReference<? extends IUICacheType<?, IUIComponentView<?, ?>>> thisRef =
												OptionalWeakReference.of(suppressThisEscapedWarning(() -> this));
										Cleaner.create(suppressThisEscapedWarning(() -> this),
												AutoSubscribingDisposable.of(
														UIEventBusEntryPoint.getBusPublisher(),
														ImmutableList.of(
																new EventBusSubscriber<UIAbstractComponentHierarchyChangeBusEvent.Parent>(
																		ImmutableSubscribeEvent.of(EventPriority.LOWEST, true),
																		ReactiveUtilities.decorateAsListener(
																				delegate -> FunctionalNextSubscriber.of(delegate,
																						event -> {
																							if (event.getStage() == EnumHookStage.POST)
																								thisRef.getOptional()
																										.ifPresent(t -> event.getComponent().getManager()
																												.flatMap(IUIComponentManager::getView)
																												.ifPresent(view -> t.invalidate(view)));
																						}
																				),
																				UIConfiguration.getInstance().getLogger()
																		)
																) {
																	@Override
																	public void onNext(UIAbstractComponentHierarchyChangeBusEvent.Parent event) {
																		onNextImpl(event);
																	}
																},
																new EventBusSubscriber<UIAbstractComponentHierarchyChangeBusEvent.View>(
																		ImmutableSubscribeEvent.of(EventPriority.LOWEST, true),
																		ReactiveUtilities.decorateAsListener(
																				delegate -> FunctionalNextSubscriber.of(delegate,
																						event -> {
																							if (event.getStage() == EnumHookStage.POST)
																								thisRef.getOptional()
																										.ifPresent(t -> event.getComponent().getManager()
																												.flatMap(IUIComponentManager::getView)
																												.ifPresent(view -> t.invalidate(view)));
																						}
																				),
																				UIConfiguration.getInstance().getLogger()
																		)
																) {
																	@Override
																	public void onNext(UIAbstractComponentHierarchyChangeBusEvent.View event) {
																		onNextImpl(event);
																	}
																}
														)
												)::dispose);
									}

									@SuppressWarnings("UnstableApiUsage")
									@Override
									protected List<? extends OptionalWeakReference<? extends IUIComponent>> load(IUIComponentView<?, ?> container) {
										return container.getChildrenFlatView().stream()
												.filter(IUIComponent::isFocusable)
												.map(OptionalWeakReference::of)
												.collect(Collectors.toCollection(ArrayList::new)); // COMMENT need mutability
									}

									@Override
									protected Optional<? extends List<? extends IUIComponent>> transform(IUIComponentView<?, ?> container,
									                                                                     @Nullable List<? extends OptionalWeakReference<? extends IUIComponent>> value) {
										assert value != null;
										return Optional.of(CollectionUtilities.collectOrRemoveReferences(value));
									}
								})));

		public static IRegistryObject<IUICacheType<List<? extends IUIComponent>, IUIComponentView<?, ?>>> getChildrenFlat() {
			return CHILDREN_FLAT;
		}

		public static IRegistryObject<IUICacheType<List<? extends IUIComponent>, IUIComponentView<?, ?>>> getChildrenFlatFocusable() {
			return CHILDREN_FLAT_FOCUSABLE;
		}
	}

	public static class ComponentHierarchyChangeParentSubscriber
			extends DelegatingSubscriber<UIAbstractComponentHierarchyChangeBusEvent.Parent> {
		private final OptionalWeakReference<UIDefaultComponentView<?, ?>> owner;

		protected ComponentHierarchyChangeParentSubscriber(Subscriber<? super UIAbstractComponentHierarchyChangeBusEvent.Parent> delegate, UIDefaultComponentView<?, ?> owner) {
			super(delegate);
			this.owner = OptionalWeakReference.of(owner);
		}

		@SuppressWarnings("AnonymousInnerClass")
		public static DisposableSubscriber<UIAbstractComponentHierarchyChangeBusEvent.Parent> ofDecorated(UIDefaultComponentView<?, ?> owner, Logger logger) {
			return new EventBusSubscriber<UIAbstractComponentHierarchyChangeBusEvent.Parent>(
					ImmutableSubscribeEvent.of(EventPriority.LOWEST, true),
					ReactiveUtilities.decorateAsListener(delegate -> new ComponentHierarchyChangeParentSubscriber(delegate, owner), logger)
			) {
				@Override
				public void onNext(UIAbstractComponentHierarchyChangeBusEvent.Parent event) {
					onNextImpl(event);
				}
			};
		}

		@Override
		@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
		@SuppressWarnings({"rawtypes", "RedundantSuppression"})
		public void onNext(UIAbstractComponentHierarchyChangeBusEvent.@Nonnull Parent event) {
			super.onNext(event);
			if (event.getStage() == EnumHookStage.POST) {
				getOwner().ifPresent(owner -> {
					IUIComponent component = event.getComponent();
					@Nullable IUIComponentView<?, ?> nextOwner = event.getNext().flatMap(IUIComponent::getManager).flatMap(IUIComponentManager::getView).orElse(null);
					if (owner.equals(nextOwner)) {
						owner.getLifecycleStateTracker().getStateApplier(EnumUILifecycleState.BOUND)
								.ifPresent(stateApplier ->
										IUIComponentView.traverseComponentTreeDefault(component,
												component1 ->
														IUIComponentStructureLifecycleModifier.handleComponentModifiers(component1,
																component1.getModifiersView(),
																stateApplier),
												FunctionUtilities.getEmptyBiConsumer())
								);
						if (owner.getLifecycleStateTracker().containsState(EnumUILifecycleState.INITIALIZED)) {
							try (IUIComponentContext componentContext = IUIComponent.createContextTo(component)) {
								IUIComponentView.<RuntimeException>traverseComponentTreeDefault(componentContext, // TODO javac bug, need explicit type arguments
										component,
										(componentContext1, result) ->
												IUIComponentActiveLifecycleModifier.handleComponentModifiers(result.getComponent(),
														result.getModifiersView(),
														modifier -> modifier.initialize(componentContext1)),
										IConsumer3.StaticHolder.getEmpty());
							}
						}
					} else {
						@Nullable IUIComponentView<?, ?> previousOwner = event.getPrevious().flatMap(IUIComponent::getManager).flatMap(IUIComponentManager::getView).orElse(null);
						if (owner.equals(previousOwner)) {
							try (IUIComponentContext componentContext = IUIComponent.createContextTo(component)) {
								IUIComponentView.<RuntimeException>traverseComponentTreeDefault(componentContext, // TODO javac bug, need explicit type arguments
										component,
										(componentContext1, result) ->
												IUIComponentActiveLifecycleModifier.handleComponentModifiers(result.getComponent(),
														result.getModifiersView(),
														modifier -> modifier.cleanup(componentContext1)),
										IConsumer3.StaticHolder.getEmpty());
							}
							IUIComponentView.traverseComponentTreeDefault(component,
									component1 ->
											IUIComponentStructureLifecycleModifier.handleComponentModifiers(component1,
													component.getModifiersView(),
													IUIStructureLifecycle::unbindV),
									FunctionUtilities.getEmptyBiConsumer());
						}
					}
				});
			}
		}

		protected Optional<? extends UIDefaultComponentView<?, ?>> getOwner() {
			return owner.getOptional();
		}
	}
}
