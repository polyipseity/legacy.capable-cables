package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.impl;

import com.google.common.collect.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.UIProperty;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUIStructureLifecycleContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIReshapeExplicitly;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.embed.IUIComponentEmbed;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventFocus;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.types.EnumUIEventDOMType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIComponentRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.UINamespaceUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.binding.UIImmutablePropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.bus.UIEventBusEntryPoint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui.UIFunctionalEventListener;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.embed.UIAbstractComponentEmbed;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.embed.UIChildlessComponentEmbed;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.embed.UIComponentEmbedUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.bus.UIComponentModifyShapeDescriptorBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering.UIDefaultRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.descriptors.SupplierShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUIRotation;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUISide;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUISideType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AffineTransformUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AutoCloseableRotator;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.impl.OneUseRunnable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IValueHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ConstantValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.DefaultValueHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBindingAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits.IHasBindingKey;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.ImmutableBindingAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.methods.ImmutableBindingMethodDestination;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.EnumHookStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.EventBusSubscriber;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.ImmutableSubscribeEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.graphics.impl.UIObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.reactive.impl.DelegatingSubscriber;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.reactive.impl.ReactiveUtilities;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NonNls;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.*;

public class UIWindowComponent
		extends UIShapeComponent
		implements IUIReshapeExplicitly<RectangularShape> {
	public static final @NonNls String PROPERTY_CONTROLS_SIDE = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.window.controls.side";
	public static final @NonNls String PROPERTY_CONTROLS_THICKNESS = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.window.controls.thickness";
	public static final @NonNls String PROPERTY_CONTROLS_DIRECTION = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.window.controls.direction";
	public static final @NonNls String INTERNAL_BINDING_CONTROLS_BUTTON_ACTIVATE_PREFIX = "window.controls.button.activate";
	public static final @NonNls String INTERNAL_BINDING_CONTROLS_BUTTON_ACTIVATED_PREFIX = "window.controls.button.activated";
	private static final IIdentifier PROPERTY_CONTROLS_SIDE_IDENTIFIER = ImmutableIdentifier.ofInterning(getPropertyControlsSide());
	private static final IIdentifier PROPERTY_CONTROLS_THICKNESS_IDENTIFIER = ImmutableIdentifier.ofInterning(getPropertyControlsThickness());
	private static final IIdentifier PROPERTY_CONTROLS_DIRECTION_IDENTIFIER = ImmutableIdentifier.ofInterning(getPropertyControlsDirection());

	public static final @NonNls String EMBED_CONTENT_PANE_NAME = "window.content_pane";

	@SuppressWarnings("ThisEscapedInObjectConstruction")
	private final AutoCloseableRotator<DisposableSubscriber<UIComponentModifyShapeDescriptorBusEvent>, RuntimeException> modifyShapeDescriptorSubscriberRotator =
			new AutoCloseableRotator<>(() -> ModifyShapeDescriptorSubscriber.ofDecorated(this, UIConfiguration.getInstance().getLogger()), Disposable::dispose);
	private final IUIRendererContainerContainer<IUIComponentRenderer<?>> rendererContainerContainer;

	@UIProperty(PROPERTY_CONTROLS_SIDE)
	private final IBindingField<EnumUISide> controlsSide;
	@UIProperty(PROPERTY_CONTROLS_THICKNESS)
	private final IBindingField<Double> controlsThickness;
	@UIProperty(PROPERTY_CONTROLS_DIRECTION)
	private final IBindingField<EnumUIRotation> controlsDirection;

	private final IUIControlsEmbed<?> controlsEmbed;
	private final IUIComponentEmbed<? extends UIScrollPanelComponent> contentPaneEmbed;

	private final Runnable eventTargetListenersInitializer;

	@UIComponentConstructor
	public UIWindowComponent(IUIComponentArguments arguments) {
		super(arguments);

		this.rendererContainerContainer =
				UIDefaultRendererContainerContainer.ofDefault(arguments.getRendererName().orElse(null), suppressThisEscapedWarning(() -> this),
						CastUtilities.castUnchecked(DefaultRenderer.class));

		@Immutable Map<IIdentifier, ? extends IUIPropertyMappingValue> mappings = arguments.getMappingsView();

		this.controlsSide = IUIPropertyMappingValue.createBindingField(EnumUISide.class, ConstantValue.of(EnumUISide.UP), mappings.get(getPropertyControlsSideIdentifier()));
		this.controlsThickness = IUIPropertyMappingValue.createBindingField(Double.class, ConstantValue.of(suppressBoxing(10D)), mappings.get(getPropertyControlsThicknessIdentifier()));
		this.controlsDirection = IUIPropertyMappingValue.createBindingField(EnumUIRotation.class, ConstantValue.of(EnumUIRotation.CLOCKWISE), mappings.get(getPropertyControlsDirectionIdentifier()));

		OptionalWeakReference<UIWindowComponent> thisReference = OptionalWeakReference.of(suppressThisEscapedWarning(() -> this));
		// COMMENT here be dragons
		this.controlsEmbed = new UIDefaultControlsEmbed<>(
				UIShapeComponent.class,
				suppressThisEscapedWarning(() -> this),
				ImmutableMap.<String, IUIComponentEmbedArguments>builder()
						.put(IUIControlsEmbed.StaticHolder.getName(),
								arguments.computeEmbedArgument(IUIControlsEmbed.StaticHolder.getName(),
										UIShapeComponent::new,
										new SupplierShapeDescriptor<>(() ->
												thisReference.getOptional()
														.map(this1 -> {
															// COMMENT renders the controls
															EnumUISide controlsSide = this1.getControlsSide().getValue();
															Rectangle2D this1Bounds = IUIComponent.getShape(this1).getBounds2D();
															Point2D contentTranslation = getContentTranslation(this1);

															Rectangle2D result = new Rectangle2D.Double(
																	-contentTranslation.getX(),
																	-contentTranslation.getY(),
																	this1Bounds.getWidth(),
																	this1Bounds.getHeight()
															);
															EnumUISide oppositeBorderSide = controlsSide.getOpposite().orElseThrow(IllegalStateException::new);
															oppositeBorderSide.setValue(result,
																	controlsSide.getValue(result)
																			+ controlsSide.inwardsBy(suppressUnboxing(this1.getControlsThickness().getValue()))
																			.orElseThrow(IllegalStateException::new));

															return result;
														})
														.orElseGet(Rectangle2D.Double::new)
										)))
						.put(EnumControlsAction.CLOSE.getName(),
								arguments.computeEmbedArgument(EnumControlsAction.CLOSE.getName(),
										arguments1 -> new UIButtonComponent(
												thisReference.getOptional()
														.<IUIComponentArguments>map(this1 -> {
															String keyPrefix = UINamespaceUtilities.getUniqueInternalBindingNamespace(this1);

															IIdentifier onActivateKey =
																	ImmutableIdentifier.ofInterning(keyPrefix,
																			getInternalBindingControlsButtonActivatePrefix() + '.' + EnumControlsAction.CLOSE.getName(),
																			false, true);
															IIdentifier onActivatedKey =
																	ImmutableIdentifier.ofInterning(keyPrefix,
																			getInternalBindingControlsButtonActivatedPrefix() + '.' + EnumControlsAction.CLOSE.getName(),
																			false, true);

															IValueHolder<IUIComponentArguments> pointerArguments = DefaultValueHolder.of(arguments1);

															if (UIComponentEmbedUtilities.withMappingsIfUndefined(pointerArguments,
																	ImmutableMap.of(
																			UIButtonComponent.getMethodOnActivateIdentifier(),
																			() -> UIImmutablePropertyMappingValue.ofKey(onActivateKey),
																			UIButtonComponent.getMethodOnActivatedIdentifier(),
																			() -> UIImmutablePropertyMappingValue.ofKey(onActivatedKey)
																	))) {
																this1.getEmbedBindings()
																		.addAll(ImmutableList.of(
																				ImmutableBindingMethodDestination.of(UIButtonComponent.IUIEventActivate.class,
																						onActivateKey,
																						event -> thisReference.getOptional()
																								.ifPresent(this2 -> this2.onControlsButtonActivate(EnumControlsAction.CLOSE, event))),
																				ImmutableBindingMethodDestination.of(IUIEvent.class,
																						onActivatedKey,
																						event -> thisReference.getOptional()
																								.ifPresent(this2 -> this2.onControlsButtonActivated(EnumControlsAction.CLOSE, event)))
																		));
															}

															return pointerArguments.getValue()
																	.orElseThrow(AssertionError::new);
														}).orElse(arguments1)
										),
										new SupplierShapeDescriptor<>(() ->
												thisReference.getOptional()
														.map(this1 -> {
															EnumUISide controlsSide = this1.getControlsSide().getValue();
															EnumUIRotation controlsRotation = this1.getControlsDirection().getValue();
															Rectangle2D controlsBounds = IUIComponent.getShape(this1.getControlsEmbed().getComponent()).getBounds2D();

															Rectangle2D result = UIObjectUtilities.unPositionRectangularShape(controlsBounds, new Rectangle2D.Double());
															double size = controlsSide.getAxis().getSize(controlsBounds);
															EnumUISide startingResultSide = controlsRotation.rotateBy(controlsSide, 1L)
																	.orElseThrow(IllegalStateException::new);
															EnumUISide endingResultSide = startingResultSide.getOpposite()
																	.orElseThrow(IllegalStateException::new);
															endingResultSide.setValue(result,
																	startingResultSide.getValue(result)
																			+ startingResultSide.inwardsBy(size)
																			.orElseThrow(IllegalStateException::new));

															return result;
														})
														.orElseGet(Rectangle2D.Double::new)
										)
								))
						.build()
		);
		this.contentPaneEmbed = new UIChildlessComponentEmbed<>(UIScrollPanelComponent.class,
				suppressThisEscapedWarning(() -> this),
				arguments.computeEmbedArgument(getEmbedContentPaneName(),
						UIScrollPanelComponent::new,
						new SupplierShapeDescriptor<>(() -> thisReference.getOptional()
								.map(UIWindowComponent::getContentBounds)
								.orElseGet(Rectangle2D.Double::new))));

		this.eventTargetListenersInitializer = new OneUseRunnable(() ->
				addEventListener(EnumUIEventDOMType.FOCUS_IN_POST.getEventType(), new UIFunctionalEventListener<IUIEventFocus>(e ->
						getParent().ifPresent(parent ->
								parent.moveChildToTop(this))
				), true)
		);
	}

	protected static Point2D getContentTranslation(UIWindowComponent instance) {
		Point2D translation = new Point2D.Double();
		EnumUISide controlsSide = instance.getControlsSide().getValue();
		if (controlsSide.getType() == EnumUISideType.LOCATION)
			controlsSide.getAxis().setCoordinate(translation, suppressUnboxing(instance.getControlsThickness().getValue()));
		return translation;
	}

	public static @Nonnull String getEmbedContentPaneName() {
		return EMBED_CONTENT_PANE_NAME;
	}

	public static IIdentifier getPropertyControlsSideIdentifier() {
		return PROPERTY_CONTROLS_SIDE_IDENTIFIER;
	}

	public static IIdentifier getPropertyControlsThicknessIdentifier() {
		return PROPERTY_CONTROLS_THICKNESS_IDENTIFIER;
	}

	public static @NonNls String getInternalBindingControlsButtonActivatePrefix() {
		return INTERNAL_BINDING_CONTROLS_BUTTON_ACTIVATE_PREFIX;
	}

	public static @NonNls String getInternalBindingControlsButtonActivatedPrefix() {
		return INTERNAL_BINDING_CONTROLS_BUTTON_ACTIVATED_PREFIX;
	}

	public static IIdentifier getPropertyControlsDirectionIdentifier() {
		return PROPERTY_CONTROLS_DIRECTION_IDENTIFIER;
	}

	protected static Rectangle2D getContentBounds(UIWindowComponent instance) {
		// COMMENT 0, 0 is after 'transformChildren'
		Rectangle2D bounds = IUIComponent.getShape(instance).getBounds2D();
		// COMMENT consider controls
		EnumUISide controlsSide = instance.getControlsSide().getValue();
		controlsSide.getAxis().setSize(bounds,
				controlsSide.getAxis().getSize(bounds)
						- suppressUnboxing(instance.getControlsThickness().getValue()));
		return UIObjectUtilities.unPositionRectangularShape(bounds, bounds);
	}

	@Override
	public IUIComponent getContentComponent() {
		return getContentPane();
	}

	protected IBindingField<EnumUISide> getControlsSide() {
		return controlsSide;
	}

	protected IBindingField<Double> getControlsThickness() {
		return controlsThickness;
	}

	protected IBindingField<EnumUIRotation> getControlsDirection() {
		return controlsDirection;
	}

	@Override
	public boolean reshape(Predicate<? super IShapeDescriptor<? super RectangularShape>> action) throws ConcurrentModificationException {
		return IUIComponent.reshapeComponent(this, getShapeDescriptor(), action);
		// TODO reshape logic
	}

	protected void onControlsButtonActivate(@SuppressWarnings({"SameParameterValue", "unused"}) EnumControlsAction action, UIButtonComponent.IUIEventActivate event) {
		UIButtonComponent.UIDefaultEventActivate.handleEventCommonly(event);
	}

	public IUIComponent getContentPane() {
		return getContentPaneEmbed().getComponent();
	}

	@SuppressWarnings("SwitchStatementWithTooFewBranches")
	protected void onControlsButtonActivated(@SuppressWarnings("SameParameterValue") EnumControlsAction action, @SuppressWarnings("unused") IUIEvent event) {
		switch (action) {
			case CLOSE:
				getParent().ifPresent(parent ->
						parent.removeChildren(Iterators.singletonIterator(this)));
				break;
			default:
				break; // COMMENT just do nothing
		}
	}

	@Override
	protected SetMultimap<IIdentifier, UIEventListenerWithParameters> getEventTargetListeners() {
		eventTargetListenersInitializer.run();
		return super.getEventTargetListeners();
	}

	@SuppressWarnings("unchecked")
	@Override
	public IShapeDescriptor<RectangularShape> getShapeDescriptor() {
		return (IShapeDescriptor<RectangularShape>) super.getShapeDescriptor(); // COMMENT should be safe, see constructor
	}

	protected IUIComponentEmbed<? extends UIScrollPanelComponent> getContentPaneEmbed() {
		return contentPaneEmbed;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	protected void bind0(IUIStructureLifecycleContext context) {
		super.bind0(context);
		IUIReshapeExplicitly.refresh(this);
	}

	protected IUIControlsEmbed<?> getControlsEmbed() {
		return controlsEmbed;
	}

	public static @NonNls String getPropertyControlsDirection() {
		return PROPERTY_CONTROLS_DIRECTION;
	}

	public static @NonNls String getPropertyControlsSide() {
		return PROPERTY_CONTROLS_SIDE;
	}

	public static @NonNls String getPropertyControlsThickness() {
		return PROPERTY_CONTROLS_THICKNESS;
	}

	@Override
	public void transformChildren(AffineTransform transform) {
		super.transformChildren(transform);
		AffineTransformUtilities.translateByPoint(transform, getContentTranslation(this));
	}

	@Override
	protected Iterable<? extends IUIComponentEmbed<?>> getComponentEmbeds() {
		return Iterables.concat(super.getComponentEmbeds(),
				ImmutableSet.of(
						getControlsEmbed(), getContentPaneEmbed() // COMMENT controls on top of content pane
				));
	}

	@Override
	protected IUIRendererContainerContainer<IUIComponentRenderer<?>> getRendererContainerContainer() {
		return rendererContainerContainer;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	protected void cleanup0(IUIComponentContext context) {
		getModifyShapeDescriptorSubscriberRotator().close();
		super.cleanup0(context);
	}

	protected AutoCloseableRotator<? extends DisposableSubscriber<? super UIComponentModifyShapeDescriptorBusEvent>, RuntimeException> getModifyShapeDescriptorSubscriberRotator() {
		return modifyShapeDescriptorSubscriberRotator;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	protected void initialize0(IUIComponentContext context) {
		super.initialize0(context);
		UIEventBusEntryPoint.<UIComponentModifyShapeDescriptorBusEvent>getBusPublisher()
				.subscribe(getModifyShapeDescriptorSubscriberRotator().get());
	}

	@Override
	public void initializeBindings(Supplier<@io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull ? extends Optional<? extends Consumer<? super IBindingAction>>> bindingActionConsumerSupplier) {
		super.initializeBindings(bindingActionConsumerSupplier);
		BindingUtilities.supplyBindingAction(bindingActionConsumerSupplier,
				() -> ImmutableBindingAction.bind(ImmutableList.of(
						getControlsSide(), getControlsThickness(), getControlsDirection()
				))
		);
	}

	public enum EnumControlsAction {
		CLOSE {
			@Override
			public @NonNls String getName() {
				return "close";
			}
		},
		;

		public abstract @NonNls String getName();
	}

	public interface IUIControlsEmbed<C extends UIShapeComponent>
			extends IUIComponentEmbed<C> {
		@Immutable Map<EnumControlsAction, ? extends IUIComponentEmbed<? extends UIButtonComponent>> getActionButtonsView();

		enum StaticHolder {
			;

			public static final @NonNls String NAME = "controls";

			public static @NonNls String getName() {
				return NAME;
			}
		}
	}

	@Override
	public void cleanupBindings() {
		getBindingActionConsumerSupplierHolder().getValue().ifPresent(bindingActionConsumer ->
				BindingUtilities.supplyBindingAction(bindingActionConsumer,
						() -> ImmutableBindingAction.unbind(ImmutableList.of(
								getControlsSide(), getControlsThickness(), getControlsDirection()
						))
				)
		);
		super.cleanupBindings();
	}

	public static class DefaultRenderer<C extends UIWindowComponent>
			extends UIShapeComponent.DefaultRenderer<C> {
		@UIRendererConstructor
		public DefaultRenderer(IUIRendererArguments arguments) {
			super(arguments);
		}
	}

	public static class UIDefaultControlsEmbed<C extends UIShapeComponent>
			extends UIAbstractComponentEmbed<C>
			implements IUIControlsEmbed<C> {
		private final Map<EnumControlsAction, IUIComponentEmbed<? extends UIButtonComponent>> actionButtons;

		@SuppressWarnings("UnstableApiUsage")
		public UIDefaultControlsEmbed(Class<C> type,
		                              IUIComponent owner,
		                              Map<? super String, ? extends IUIComponentEmbedArguments> arguments) {
			super(type, owner, AssertionUtilities.assertNonnull(arguments.get(StaticHolder.getName())));
			this.actionButtons = Maps.immutableEnumMap(Maps.asMap(EnumSet.allOf(EnumControlsAction.class),
					key -> {
						assert key != null;
						return new UIChildlessComponentEmbed<>(
								UIButtonComponent.class,
								getComponent(),
								AssertionUtilities.assertNonnull(arguments.get(key.getName()))
						);
					}
			));
		}

		@SuppressWarnings("UnstableApiUsage")
		@Override
		public @Immutable Map<EnumControlsAction, ? extends IUIComponentEmbed<? extends UIButtonComponent>> getActionButtonsView() {
			return Maps.immutableEnumMap(getActionButtons());
		}

		protected Map<EnumControlsAction, ? extends IUIComponentEmbed<? extends UIButtonComponent>> getActionButtons() {
			return actionButtons;
		}

		@Override
		public List<? extends IUIComponentEmbed<?>> getChildrenView() {
			return ImmutableList.copyOf(getActionButtons().values());
		}
	}

	public static class ModifyShapeDescriptorSubscriber
			extends DelegatingSubscriber<UIComponentModifyShapeDescriptorBusEvent> {
		private final OptionalWeakReference<UIWindowComponent> owner;

		protected ModifyShapeDescriptorSubscriber(Subscriber<? super UIComponentModifyShapeDescriptorBusEvent> delegate, UIWindowComponent owner) {
			super(delegate);
			this.owner = OptionalWeakReference.of(owner);
		}

		@SuppressWarnings("AnonymousInnerClass")
		public static DisposableSubscriber<UIComponentModifyShapeDescriptorBusEvent> ofDecorated(UIWindowComponent owner, Logger logger) {
			return new EventBusSubscriber<UIComponentModifyShapeDescriptorBusEvent>(
					ImmutableSubscribeEvent.of(EventPriority.LOWEST, true),
					ReactiveUtilities.decorateAsListener(delegate -> new ModifyShapeDescriptorSubscriber(delegate, owner), logger)
			) {
				@Override
				public void onNext(UIComponentModifyShapeDescriptorBusEvent event) {
					onNextImpl(event);
				}
			};
		}

		@Override
		@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
		public void onNext(UIComponentModifyShapeDescriptorBusEvent event) {
			super.onNext(event);
			if (event.getStage() == EnumHookStage.POST)
				getOwner()
						.filter(owner -> owner.getParent().filter(p -> p.equals(event.getComponent())).isPresent())
						.ifPresent(IUIReshapeExplicitly::refresh);
		}

		protected Optional<? extends UIWindowComponent> getOwner() { return owner.getOptional(); }
	}
}
