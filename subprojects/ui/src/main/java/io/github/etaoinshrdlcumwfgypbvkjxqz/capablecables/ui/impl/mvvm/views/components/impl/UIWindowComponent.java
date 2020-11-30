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
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics.AutoCloseableGraphics2D;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.embed.UIAbstractComponentEmbed;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.embed.UIChildlessComponentEmbed;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.bus.UIComponentModifyShapeDescriptorBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering.UIDefaultComponentRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering.UIDefaultRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.descriptors.SupplierShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUIRotation;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUISide;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUISideType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AutoCloseableRotator;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.CollectionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.impl.OneUseRunnable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.LoggingDisposableObserver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ConstantValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinding;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits.IHasBindingKey;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.ImmutableBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.methods.ImmutableBindingMethodDestination;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.EnumHookStage;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NonNls;
import org.slf4j.Logger;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.List;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.*;

public class UIWindowComponent
		extends UIDefaultComponent
		implements IUIReshapeExplicitly<RectangularShape> {
	// TODO scroll bars

	public static final @NonNls String PROPERTY_CONTROLS_SIDE = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.window.controls.side";
	public static final @NonNls String PROPERTY_CONTROLS_THICKNESS = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.window.controls.thickness";
	public static final @NonNls String PROPERTY_CONTROLS_DIRECTION = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.window.controls.direction";
	public static final @NonNls String INTERNAL_BINDING_ON_ACTIVATE_PREFIX = "controls.button.activate";
	public static final @NonNls String INTERNAL_BINDING_ON_ACTIVATED_PREFIX = "controls.button.activated";
	private static final INamespacePrefixedString PROPERTY_CONTROLS_SIDE_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyControlsSide());
	private static final INamespacePrefixedString PROPERTY_CONTROLS_THICKNESS_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyControlsThickness());
	private static final INamespacePrefixedString PROPERTY_CONTROLS_DIRECTION_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyControlsDirection());

	@SuppressWarnings("ThisEscapedInObjectConstruction")
	private final AutoCloseableRotator<ModifyShapeDescriptorObserver, RuntimeException> modifyShapeDescriptorObserver =
			new AutoCloseableRotator<>(() -> new ModifyShapeDescriptorObserver(this, UIConfiguration.getInstance().getLogger()), Disposable::dispose);
	private final IUIRendererContainerContainer<IUIComponentRenderer<?>> rendererContainerContainer;

	@UIProperty(PROPERTY_CONTROLS_SIDE)
	private final IBindingField<EnumUISide> controlsSide;
	@UIProperty(PROPERTY_CONTROLS_THICKNESS)
	private final IBindingField<Double> controlsThickness;
	@UIProperty(PROPERTY_CONTROLS_DIRECTION)
	private final IBindingField<EnumUIRotation> controlsDirection;

	private final IUIControlsEmbed<?> controlsEmbed;
	private final List<IBinding<?>> embedBindings = new ArrayList<>(CapacityUtilities.getInitialCapacitySmall());

	private final Runnable eventTargetListenersInitializer;

	@SuppressWarnings({"rawtypes", "RedundantSuppression"})
	@UIComponentConstructor
	public UIWindowComponent(IUIComponentArguments arguments) {
		super(arguments);

		this.rendererContainerContainer =
				UIDefaultRendererContainerContainer.ofDefault(arguments.getRendererName().orElse(null), suppressThisEscapedWarning(() -> this),
						CastUtilities.castUnchecked(DefaultRenderer.class));

		Map<INamespacePrefixedString, ? extends IUIPropertyMappingValue> mappings = arguments.getMappingsView();
		this.controlsSide = IUIPropertyMappingValue.createBindingField(EnumUISide.class, ConstantValue.of(EnumUISide.UP), mappings.get(getPropertyControlsSideLocation()));
		this.controlsThickness = IUIPropertyMappingValue.createBindingField(Double.class, ConstantValue.of(suppressBoxing(10D)), mappings.get(getPropertyControlsThicknessLocation()));
		this.controlsDirection = IUIPropertyMappingValue.createBindingField(EnumUIRotation.class, ConstantValue.of(EnumUIRotation.CLOCKWISE), mappings.get(getPropertyControlsDirectionLocation()));

		this.eventTargetListenersInitializer = new OneUseRunnable(() ->
				addEventListener(EnumUIEventDOMType.FOCUS_IN_POST.getEventType(), new UIFunctionalEventListener<IUIEventFocus>(e ->
						getParent().ifPresent(parent ->
								parent.moveChildToTop(this))
				), true)
		);

		OptionalWeakReference<UIWindowComponent> thisReference = OptionalWeakReference.of(suppressThisEscapedWarning(() -> this));
		// COMMENT here be dragons
		this.controlsEmbed = new UIDefaultControlsEmbed<>(
				UIShapeComponent.class,
				suppressThisEscapedWarning(() -> this),
				ImmutableMap.<String, IUIComponentEmbedArguments>builder()
						.put(IUIControlsEmbed.StaticHolder.getName(),
								arguments.computeEmbedArgument(IUIControlsEmbed.StaticHolder.getName(), UIShapeComponent::new,
										new SupplierShapeDescriptor<>(() ->
												thisReference.getOptional()
														.map(this1 -> {
															// COMMENT renders the controls
															EnumUISide controlsSide = this1.getControlsSide().getValue();
															Rectangle2D this1Bounds = IUIComponent.getShape(this1).getBounds2D();
															Point2D contentTranslation = getWindowContentTranslation(this1);

															Rectangle2D result = new Rectangle2D.Double(-contentTranslation.getX(), -contentTranslation.getY(),
																	this1Bounds.getWidth(),
																	this1Bounds.getHeight());
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
										arguments1 -> {
											IUIComponentArguments arguments2;
											Map<INamespacePrefixedString, ? extends IUIPropertyMappingValue> mappings1 = arguments1.getMappingsView();
											if (mappings1.containsKey(UIButtonComponent.getMethodOnActivateLocation())
													|| mappings1.containsKey(UIButtonComponent.getMethodOnActivatedLocation())) {
												arguments2 = arguments1;
											} else {
												arguments2 = thisReference.getOptional()
														.map(this1 -> {
															String keyPrefix = UINamespaceUtilities.getUniqueInternalBindingNamespace(this1);

															INamespacePrefixedString onActivateKey =
																	ImmutableNamespacePrefixedString.of(keyPrefix,
																			getInternalBindingOnActivatePrefix() + '.' + EnumControlsAction.CLOSE.getName());
															INamespacePrefixedString onActivatedKey =
																	ImmutableNamespacePrefixedString.of(keyPrefix,
																			getInternalBindingOnActivatedPrefix() + '.' + EnumControlsAction.CLOSE.getName());

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

															return arguments1.withMappings(
																	MapUtilities.concatMaps(mappings1,
																			ImmutableMap.of(
																					UIButtonComponent.getMethodOnActivateLocation(),
																					UIImmutablePropertyMappingValue.of(null, onActivateKey),
																					UIButtonComponent.getMethodOnActivatedLocation(),
																					UIImmutablePropertyMappingValue.of(null, onActivatedKey)
																			))
															);
														})
														.orElse(arguments1);
											}
											return new UIButtonComponent(arguments2);
										},
										new SupplierShapeDescriptor<>(() ->
												thisReference.getOptional()
														.map(this1 -> {
															EnumUISide controlsSide = this1.getControlsSide().getValue();
															EnumUIRotation controlsRotation = this1.getControlsDirection().getValue();
															Rectangle2D controlsBounds = IUIComponent.getShape(this1.getControlsEmbed().getComponent()).getBounds2D();

															Rectangle2D result = new Rectangle2D.Double(0D, 0D, controlsBounds.getWidth(), controlsBounds.getHeight());
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
	}

	public static INamespacePrefixedString getPropertyControlsSideLocation() {
		return PROPERTY_CONTROLS_SIDE_LOCATION;
	}

	public static INamespacePrefixedString getPropertyControlsThicknessLocation() {
		return PROPERTY_CONTROLS_THICKNESS_LOCATION;
	}

	public static INamespacePrefixedString getPropertyControlsDirectionLocation() {
		return PROPERTY_CONTROLS_DIRECTION_LOCATION;
	}

	public static @NonNls String getInternalBindingOnActivatePrefix() {
		return INTERNAL_BINDING_ON_ACTIVATE_PREFIX;
	}

	public static @NonNls String getInternalBindingOnActivatedPrefix() {
		return INTERNAL_BINDING_ON_ACTIVATED_PREFIX;
	}

	protected IBindingField<EnumUISide> getControlsSide() {
		return controlsSide;
	}

	protected static Point2D getWindowContentTranslation(UIWindowComponent instance) {
		Point2D translation = new Point2D.Double();
		EnumUISide controlsSide = instance.getControlsSide().getValue();
		if (controlsSide.getType() == EnumUISideType.LOCATION)
			controlsSide.getAxis().setCoordinate(translation, suppressUnboxing(instance.getControlsThickness().getValue()));
		return translation;
	}

	protected IBindingField<Double> getControlsThickness() {
		return controlsThickness;
	}

	protected IBindingField<EnumUIRotation> getControlsDirection() {
		return controlsDirection;
	}

	@Override
	public boolean addChildren(Iterable<? extends IUIComponent> components) {
		// COMMENT add components before the controls so that the controls can draw above all others
		return addChildrenImpl(this,
				(self, child) ->
						CollectionUtilities.indexOf(self.getChildren(), self.getControlsEmbed().getComponent())
								.orElseGet(self.getChildren()::size),
				components);
	}

	@Override
	public boolean reshape(Predicate<? super IShapeDescriptor<? super RectangularShape>> action) throws ConcurrentModificationException {
		return IUIComponent.reshapeComponent(this, getShapeDescriptor(), action);
		// TODO reshape logic
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected List<IBinding<?>> getEmbedBindings() {
		return embedBindings;
	}

	protected void onControlsButtonActivate(@SuppressWarnings({"SameParameterValue", "unused"}) EnumControlsAction action, UIButtonComponent.IUIEventActivate event) {
		UIButtonComponent.UIDefaultEventActivate.handleEventCommonly(event);
	}

	@SuppressWarnings("SwitchStatementWithTooFewBranches")
	protected void onControlsButtonActivated(@SuppressWarnings("SameParameterValue") EnumControlsAction action, @SuppressWarnings("unused") IUIEvent event) {
		switch (action) {
			case CLOSE:
				getParent().ifPresent(parent ->
						parent.removeChildren(ImmutableSet.of(this)));
				break;
			default:
				break; // COMMENT just do nothing
		}
	}

	@Override
	protected SetMultimap<INamespacePrefixedString, UIEventListenerWithParameters> getEventTargetListeners() {
		eventTargetListenersInitializer.run();
		return super.getEventTargetListeners();
	}

	@Override
	protected List<IUIComponent> getChildren() {
		getControlsEmbed().getEmbedInitializer().run();
		return super.getChildren();
	}

	@SuppressWarnings("unchecked")
	@Override
	public IShapeDescriptor<RectangularShape> getShapeDescriptor() {
		return (IShapeDescriptor<RectangularShape>) super.getShapeDescriptor(); // COMMENT should be safe, see constructor
	}

	@Override
	public void transformChildren(AffineTransform transform) {
		super.transformChildren(transform);
		Point2D translation = getWindowContentTranslation(this);
		transform.translate(translation.getX(), translation.getY());
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
	public void initializeBindings(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		super.initializeBindings(binderObserverSupplier);
		BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
				() -> ImmutableBinderAction.bind(Iterables.concat(
						ImmutableList.of(getControlsSide(), getControlsThickness(), getControlsDirection()),
						getEmbedBindings()
				)));
	}

	@Override
	public void cleanupBindings() {
		getBinderObserverSupplierHolder().getValue().ifPresent(binderObserverSupplier ->
				BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
						() -> ImmutableBinderAction.unbind(Iterables.concat(
								ImmutableList.of(getControlsSide(), getControlsThickness(), getControlsDirection()),
								getEmbedBindings()
						)))
		);
		super.cleanupBindings();
	}

	@Override
	protected IUIRendererContainerContainer<IUIComponentRenderer<?>> getRendererContainerContainer() {
		return rendererContainerContainer;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	protected void cleanup0(IUIComponentContext context) {
		getModifyShapeDescriptorObserver().close();
		super.cleanup0(context);
	}

	protected AutoCloseableRotator<ModifyShapeDescriptorObserver, RuntimeException> getModifyShapeDescriptorObserver() { return modifyShapeDescriptorObserver; }

	@Override
	@OverridingMethodsMustInvokeSuper
	protected void initialize0(IUIComponentContext context) {
		super.initialize0(context);
		UIEventBusEntryPoint.<UIComponentModifyShapeDescriptorBusEvent>getEventBus()
				.subscribe(getModifyShapeDescriptorObserver().get());
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

	protected static class ModifyShapeDescriptorObserver
			extends LoggingDisposableObserver<UIComponentModifyShapeDescriptorBusEvent> {
		private final OptionalWeakReference<UIWindowComponent> owner;

		public ModifyShapeDescriptorObserver(UIWindowComponent owner, Logger logger) {
			super(logger);
			this.owner = OptionalWeakReference.of(owner);
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

	public static class DefaultRenderer<C extends UIWindowComponent>
			extends UIDefaultComponentRenderer<C> {
		@NonNls
		public static final String PROPERTY_BACKGROUND_COLOR = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.window.background.color";

		private static final INamespacePrefixedString PROPERTY_BACKGROUND_COLOR_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyBackgroundColor());

		@UIProperty(PROPERTY_BACKGROUND_COLOR)
		private final IBindingField<Color> backgroundColor; // TODO Color to Paint

		@UIRendererConstructor
		public DefaultRenderer(IUIRendererArguments arguments) {
			super(arguments);

			Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = arguments.getMappingsView();
			this.backgroundColor = IUIPropertyMappingValue.createBindingField(Color.class, ConstantValue.of(Color.BLACK), mappings.get(getPropertyBackgroundColorLocation()));
		}

		public static INamespacePrefixedString getPropertyBackgroundColorLocation() {
			return PROPERTY_BACKGROUND_COLOR_LOCATION;
		}

		public static String getPropertyBackgroundColor() {
			return PROPERTY_BACKGROUND_COLOR;
		}

		@Override
		@OverridingMethodsMustInvokeSuper
		public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
			super.initializeBindings(binderObserverSupplier);
			BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
					() -> ImmutableBinderAction.bind(
							getBackgroundColor()
					)
			);
		}

		@Override
		@OverridingMethodsMustInvokeSuper
		public void cleanupBindings() {
			getBinderObserverSupplierHolder().getValue().ifPresent(binderObserverSupplier ->
					BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
							() -> ImmutableBinderAction.unbind(
									getBackgroundColor()
							)
					)
			);
			super.cleanupBindings();
		}

		public IBindingField<Color> getBackgroundColor() { return backgroundColor; }

		@Override
		public void render(IUIComponentContext context, EnumRenderStage stage) {
			super.render(context, stage);
			getContainer().ifPresent(container -> {
				if (stage == EnumRenderStage.PRE_CHILDREN) {
					Shape relativeShape = IUIComponent.getShape(container);
					try (AutoCloseableGraphics2D graphics = AutoCloseableGraphics2D.of(context.createGraphics())) {
						graphics.setColor(getBackgroundColor().getValue());
						graphics.fill(relativeShape);
					}
				}
			});
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
}
