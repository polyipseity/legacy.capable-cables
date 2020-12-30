package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.SetMultimap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.UIMethod;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.UIProperty;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.IUIComponentArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.IUIRendererArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.UIComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentCursorHandleProviderModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.types.EnumUIEventDOMType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIComponentRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.cursors.EnumGLFWCursor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics.AutoCloseableGraphics2D;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering.UIDefaultComponentRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering.UIDefaultRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.impl.OneUseRunnable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ConstantValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBindingAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.methods.IBindingMethodSource;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits.IHasBindingKey;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.ImmutableBindingAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.methods.ImmutableBindingMethodSource;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.ICursor;
import org.jetbrains.annotations.NonNls;
import org.lwjgl.glfw.GLFW;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressThisEscapedWarning;

public class UIButtonComponent
		extends UIDefaultComponent
		implements IUIComponentCursorHandleProviderModifier {
	@NonNls
	public static final String METHOD_ON_ACTIVATE = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "method.button.activate";
	@NonNls
	public static final String METHOD_ON_ACTIVATED = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "method.button.activated";
	@NonNls
	public static final String METHOD_ON_CANCELED = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "method.button.canceled";

	private static final IIdentifier METHOD_ON_ACTIVATE_IDENTIFIER = ImmutableIdentifier.ofInterning(getMethodOnActivate());
	private static final IIdentifier METHOD_ON_ACTIVATED_IDENTIFIER = ImmutableIdentifier.ofInterning(getMethodOnActivated());
	private static final IIdentifier METHOD_ON_CANCELED_IDENTIFIER = ImmutableIdentifier.ofInterning(getMethodOnCanceled());

	@UIMethod(METHOD_ON_ACTIVATE)
	private final IBindingMethodSource<IUIEventActivate> onActivate;
	@UIMethod(METHOD_ON_ACTIVATED)
	private final IBindingMethodSource<IUIEvent> onActivated;
	@UIMethod(METHOD_ON_CANCELED)
	private final IBindingMethodSource<IUIEvent> onCanceled;

	private final Set<EnumButtonState> buttonStates = Collections.newSetFromMap(
			MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(EnumButtonState.values().length).makeMap()
	);

	private final IUIRendererContainerContainer<IUIComponentRenderer<?>> rendererContainerContainer;

	private final Runnable eventTargetListenersInitializer;

	@UIComponentConstructor
	public UIButtonComponent(IUIComponentArguments arguments) {
		super(arguments);

		Map<IIdentifier, ? extends IUIPropertyMappingValue> mappings = arguments.getMappingsView();
		this.onActivate = ImmutableBindingMethodSource.of(IUIEventActivate.class,
				Optional.ofNullable(mappings.get(getMethodOnActivateIdentifier())).flatMap(IUIPropertyMappingValue::getBindingKey).orElse(null));
		this.onActivated = ImmutableBindingMethodSource.of(IUIEvent.class,
				Optional.ofNullable(mappings.get(getMethodOnActivatedIdentifier())).flatMap(IUIPropertyMappingValue::getBindingKey).orElse(null));
		this.onCanceled = ImmutableBindingMethodSource.of(IUIEvent.class,
				Optional.ofNullable(mappings.get(getMethodOnCanceledIdentifier())).flatMap(IUIPropertyMappingValue::getBindingKey).orElse(null));

		this.rendererContainerContainer =
				UIDefaultRendererContainerContainer.ofDefault(arguments.getRendererName().orElse(null), suppressThisEscapedWarning(() -> this),
						CastUtilities.castUnchecked(DefaultRenderer.class));

		this.eventTargetListenersInitializer = new OneUseRunnable(() -> {
			addEventListener(EnumUIEventDOMType.MOUSE_ENTER_SELF.getEventType(),
					UIPhasedDelegatingEventListener.of(IUIEvent.EnumPhase.AT_TARGET,
							new UIFunctionalEventListener<IUIEventMouse>(event -> getButtonStates().add(EnumButtonState.HOVERING))),
					false);
			addEventListener(EnumUIEventDOMType.MOUSE_LEAVE_SELF.getEventType(),
					UIPhasedDelegatingEventListener.of(IUIEvent.EnumPhase.AT_TARGET,
							new UIFunctionalEventListener<IUIEventMouse>(event -> getButtonStates().remove(EnumButtonState.HOVERING))),
					false);

			addEventListener(EnumUIEventDOMType.MOUSE_DOWN.getEventType(), new UIFunctionalEventListener<IUIEventMouse>(e -> {
				if (IUIEventActivate.shouldActivate(this, e)) {
					getButtonStates().add(EnumButtonState.PRESSING);
					e.stopPropagation();
				}
			}), false);
			addEventListener(EnumUIEventDOMType.MOUSE_UP.getEventType(), new UIFunctionalEventListener<IUIEventMouse>(e -> {
				if (getButtonStates().remove(EnumButtonState.PRESSING)) {
					if (getButtonStates().contains(EnumButtonState.HOVERING)) {
						getOnActivated().invoke(e);
					} else {
						getOnCanceled().invoke(e);
					}
					e.stopPropagation();
				}
			}), false);

			addEventListener(EnumUIEventDOMType.KEY_DOWN.getEventType(), new UIFunctionalEventListener<IUIEventKeyboard>(e -> {
				if (IUIEventActivate.shouldActivate(this, e)) {
					getOnActivated().invoke(e);
					e.stopPropagation();
				}
			}), false);
		});
	}

	public static IIdentifier getMethodOnActivateIdentifier() {
		return METHOD_ON_ACTIVATE_IDENTIFIER;
	}

	public static IIdentifier getMethodOnActivatedIdentifier() {
		return METHOD_ON_ACTIVATED_IDENTIFIER;
	}

	public static IIdentifier getMethodOnCanceledIdentifier() {
		return METHOD_ON_CANCELED_IDENTIFIER;
	}

	protected IBindingMethodSource<IUIEvent> getOnCanceled() {
		return onCanceled;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Set<EnumButtonState> getButtonStates() { return buttonStates; }

	protected IBindingMethodSource<IUIEvent> getOnActivated() {
		return onActivated;
	}

	public static String getMethodOnActivate() {
		return METHOD_ON_ACTIVATE;
	}

	public static String getMethodOnActivated() {
		return METHOD_ON_ACTIVATED;
	}

	public static String getMethodOnCanceled() {
		return METHOD_ON_CANCELED;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends Consumer<? super IBindingAction>>> bindingActionConsumerSupplier) {
		super.initializeBindings(bindingActionConsumerSupplier);
		BindingUtilities.supplyBindingAction(bindingActionConsumerSupplier,
				() -> ImmutableBindingAction.bind(ImmutableList.of(
						getOnActivate(), getOnActivated(), getOnCanceled()
				)));
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void cleanupBindings() {
		getBindingActionConsumerSupplierHolder().getValue().ifPresent(bindingActionConsumer ->
				BindingUtilities.supplyBindingAction(bindingActionConsumer,
						() -> ImmutableBindingAction.unbind(ImmutableList.of(
								getOnActivate(), getOnActivated(), getOnCanceled()
						)))
		);
		super.cleanupBindings();
	}

	@Override
	protected IUIRendererContainerContainer<IUIComponentRenderer<?>> getRendererContainerContainer() {
		return rendererContainerContainer;
	}

	protected IBindingMethodSource<IUIEventActivate> getOnActivate() { return onActivate; }

	@Override
	public boolean isFocusable() { return true; }

	@Override
	protected SetMultimap<IIdentifier, UIEventListenerWithParameters> getEventTargetListeners() {
		eventTargetListenersInitializer.run();
		return super.getEventTargetListeners();
	}

	@Override
	public Optional<? extends ICursor> getCursorHandle(IUIComponentContext context) {
		return getButtonStates().contains(EnumButtonState.HOVERING)
				? Optional.of(EnumGLFWCursor.STANDARD_HAND_CURSOR)
				: Optional.empty();
	}

	public enum EnumButtonState {
		HOVERING,
		PRESSING,
	}

	public interface IUIEventActivate extends IUIEvent {
		// TODO move this out of UIButtonComponent, generalize this as well, view model should not depend on view
		static boolean shouldActivate(UIButtonComponent self, IUIEvent event) {
			// COMMENT false means default, which is to do nothing, prevented
			return !UIEventUtilities.dispatchEvent(new UIDefaultEventActivate((Functional) e -> {
				self.getOnActivate().invoke((IUIEventActivate) e);
				return true; // COMMENT did work
			}, event));
		}

		IUIEvent getCause();

		enum StaticHolder {
			;

			public static final @NonNls String TYPE_STRING = IUIEventType.StaticHolder.DEFAULT_PREFIX + "component.button.activated";
			private static final IIdentifier TYPE = ImmutableIdentifier.ofInterning(getTypeString());

			public static String getTypeString() {
				return TYPE_STRING;
			}

			public static IIdentifier getType() {
				return TYPE;
			}
		}
	}

	public static class UIDefaultEventActivate
			extends UIDefaultEvent
			implements IUIEventActivate {
		static {
			UIEventRegistry.getInstance().register(StaticHolder.getType(), IUIEventActivate.class); // COMMENT custom: button will be activated
		}

		private final IUIEvent cause;

		public UIDefaultEventActivate(IUIEventTarget target, IUIEvent cause) {
			super(StaticHolder.getType(), false, true, cause.getViewContext(), target);
			this.cause = cause;
		}

		public static void handleEventCommonly(IUIEventActivate event) {
			// TODO ZeroOneInfinity
			handleMouseEventCommonly(event);
			handleKeyboardEventCommonly(event);
		}

		public static void handleMouseEventCommonly(IUIEventActivate event) {
			if (!event.isDefaultPrevented()) {
				IUIEvent eventCause = event.getCause();
				if (eventCause instanceof IUIEventMouse) {
					IUIEventMouse eventCause1 = (IUIEventMouse) eventCause;
					if (eventCause1.getData().getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT)
						event.preventDefault();
				}
			}
		}

		public static void handleKeyboardEventCommonly(IUIEventActivate event) {
			if (!event.isDefaultPrevented()) {
				IUIEvent eventCause = event.getCause();
				if (eventCause instanceof IUIEventKeyboard) {
					IUIEventKeyboard eventCause1 = (IUIEventKeyboard) eventCause;
					if (eventCause1.getData().getKey() == GLFW.GLFW_KEY_ENTER)
						event.preventDefault();
				}
			}
		}

		@Override
		public IUIEvent getCause() { return cause; }
	}

	public static class DefaultRenderer<C extends UIButtonComponent>
			extends UIDefaultComponentRenderer<C> {
		@NonNls
		public static final String PROPERTY_BASE_COLOR = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.button.base.color";
		@NonNls
		public static final String PROPERTY_BASE_BORDER_COLOR = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.button.base.border.color";
		@NonNls
		public static final String PROPERTY_HOVERING_COLOR = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.button.hovering.color";
		@NonNls
		public static final String PROPERTY_HOVERING_BORDER_COLOR = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.button.hovering.border.color";
		@NonNls
		public static final String PROPERTY_PRESSED_COLOR = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.button.pressed.color";
		@NonNls
		public static final String PROPERTY_PRESSED_BORDER_COLOR = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.button.pressed.border.color";

		private static final IIdentifier PROPERTY_BASE_COLOR_IDENTIFIER = ImmutableIdentifier.ofInterning(getPropertyBaseColor());
		private static final IIdentifier PROPERTY_BASE_BORDER_COLOR_IDENTIFIER = ImmutableIdentifier.ofInterning(getPropertyBaseBorderColor());
		private static final IIdentifier PROPERTY_HOVERING_COLOR_IDENTIFIER = ImmutableIdentifier.ofInterning(getPropertyHoveringColor());
		private static final IIdentifier PROPERTY_HOVERING_BORDER_COLOR_IDENTIFIER = ImmutableIdentifier.ofInterning(getPropertyHoveringBorderColor());
		private static final IIdentifier PROPERTY_PRESSED_COLOR_IDENTIFIER = ImmutableIdentifier.ofInterning(getPropertyPressedColor());
		private static final IIdentifier PROPERTY_PRESSED_BORDER_COLOR_IDENTIFIER = ImmutableIdentifier.ofInterning(getPropertyPressedBorderColor());

		@UIProperty(PROPERTY_BASE_COLOR)
		private final IBindingField<Color> baseColor;
		@UIProperty(PROPERTY_BASE_BORDER_COLOR)
		private final IBindingField<Color> baseBorderColor;
		@UIProperty(PROPERTY_HOVERING_COLOR)
		private final IBindingField<Color> hoveringColor;
		@UIProperty(PROPERTY_HOVERING_BORDER_COLOR)
		private final IBindingField<Color> hoveringBorderColor;
		@UIProperty(PROPERTY_PRESSED_COLOR)
		private final IBindingField<Color> pressedColor;
		@UIProperty(PROPERTY_PRESSED_BORDER_COLOR)
		private final IBindingField<Color> pressedBorderColor;

		@UIRendererConstructor
		public DefaultRenderer(IUIRendererArguments arguments) {
			super(arguments);

			Map<IIdentifier, IUIPropertyMappingValue> mappings = arguments.getMappingsView();
			this.baseColor = IUIPropertyMappingValue.createBindingField(Color.class, ConstantValue.of(Color.DARK_GRAY), mappings.get(getPropertyBaseColorIdentifier()));
			this.baseBorderColor = IUIPropertyMappingValue.createBindingField(Color.class, ConstantValue.of(Color.DARK_GRAY), mappings.get(getPropertyBaseBorderColorIdentifier()));
			this.hoveringColor = IUIPropertyMappingValue.createBindingField(Color.class, ConstantValue.of(Color.GRAY), mappings.get(getPropertyHoveringColorIdentifier()));
			this.hoveringBorderColor = IUIPropertyMappingValue.createBindingField(Color.class, ConstantValue.of(Color.GRAY), mappings.get(getPropertyHoveringBorderColorIdentifier()));
			this.pressedColor = IUIPropertyMappingValue.createBindingField(Color.class, ConstantValue.of(Color.LIGHT_GRAY), mappings.get(getPropertyPressedColorIdentifier()));
			this.pressedBorderColor = IUIPropertyMappingValue.createBindingField(Color.class, ConstantValue.of(Color.LIGHT_GRAY), mappings.get(getPropertyPressedBorderColorIdentifier()));
		}

		public static IIdentifier getPropertyBaseColorIdentifier() {
			return PROPERTY_BASE_COLOR_IDENTIFIER;
		}

		public static IIdentifier getPropertyBaseBorderColorIdentifier() {
			return PROPERTY_BASE_BORDER_COLOR_IDENTIFIER;
		}

		public static IIdentifier getPropertyHoveringColorIdentifier() {
			return PROPERTY_HOVERING_COLOR_IDENTIFIER;
		}

		public static IIdentifier getPropertyHoveringBorderColorIdentifier() {
			return PROPERTY_HOVERING_BORDER_COLOR_IDENTIFIER;
		}

		public static IIdentifier getPropertyPressedColorIdentifier() {
			return PROPERTY_PRESSED_COLOR_IDENTIFIER;
		}

		public static IIdentifier getPropertyPressedBorderColorIdentifier() {
			return PROPERTY_PRESSED_BORDER_COLOR_IDENTIFIER;
		}

		public static String getPropertyBaseColor() {
			return PROPERTY_BASE_COLOR;
		}

		public static String getPropertyBaseBorderColor() {
			return PROPERTY_BASE_BORDER_COLOR;
		}

		public static String getPropertyHoveringColor() {
			return PROPERTY_HOVERING_COLOR;
		}

		public static String getPropertyHoveringBorderColor() {
			return PROPERTY_HOVERING_BORDER_COLOR;
		}

		public static String getPropertyPressedColor() {
			return PROPERTY_PRESSED_COLOR;
		}

		public static String getPropertyPressedBorderColor() {
			return PROPERTY_PRESSED_BORDER_COLOR;
		}

		@Override
		@OverridingMethodsMustInvokeSuper
		public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends Consumer<? super IBindingAction>>> bindingActionConsumerSupplier) {
			super.initializeBindings(bindingActionConsumerSupplier);
			BindingUtilities.supplyBindingAction(bindingActionConsumerSupplier,
					() -> ImmutableBindingAction.bind(ImmutableList.of(
							getBaseColor(), getBaseBorderColor(),
							getHoveringColor(), getHoveringBorderColor(),
							getPressedColor(), getPressedBorderColor()
					)));
		}

		@Override
		@OverridingMethodsMustInvokeSuper
		public void cleanupBindings() {
			getBindingActionConsumerSupplierHolder().getValue().ifPresent(bindingActionConsumer ->
					BindingUtilities.supplyBindingAction(bindingActionConsumer,
							() -> ImmutableBindingAction.unbind(ImmutableList.of(
									getBaseColor(), getBaseBorderColor(),
									getHoveringColor(), getHoveringBorderColor(),
									getPressedColor(), getPressedBorderColor()
							))));
			super.cleanupBindings();
		}

		protected IBindingField<Color> getBaseColor() {
			return baseColor;
		}

		protected IBindingField<Color> getBaseBorderColor() {
			return baseBorderColor;
		}

		protected IBindingField<Color> getHoveringColor() {
			return hoveringColor;
		}

		protected IBindingField<Color> getHoveringBorderColor() {
			return hoveringBorderColor;
		}

		protected IBindingField<Color> getPressedColor() { return pressedColor; }

		protected IBindingField<Color> getPressedBorderColor() {
			return pressedBorderColor;
		}

		@Override
		public void render(IUIComponentContext context, EnumRenderStage stage) {
			super.render(context, stage);
			getContainer().ifPresent(container -> {
				if (stage == EnumRenderStage.PRE_CHILDREN) {
					Shape relativeShape = IUIComponent.getShape(container);
					Color filled, border;
					if (container.getButtonStates().contains(EnumButtonState.PRESSING)) {
						filled = getPressedColor().getValue();
						border = getPressedBorderColor().getValue();
					} else if (container.getButtonStates().contains(EnumButtonState.HOVERING)) {
						filled = getHoveringColor().getValue();
						border = getHoveringBorderColor().getValue();
					} else {
						filled = getBaseColor().getValue();
						border = getBaseBorderColor().getValue();
					}
					try (AutoCloseableGraphics2D graphics = AutoCloseableGraphics2D.of(context.createGraphics())) {
						graphics.setColor(filled);
						graphics.fill(relativeShape);

						graphics.setColor(border);
						graphics.draw(relativeShape);
					}
				}
			});
		}
	}
}
