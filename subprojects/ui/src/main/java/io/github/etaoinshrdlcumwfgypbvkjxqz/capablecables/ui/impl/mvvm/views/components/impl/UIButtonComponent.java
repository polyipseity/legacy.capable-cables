package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.impl;

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
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui.UIDefaultEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui.UIEventRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui.UIEventUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui.UIFunctionalEventListener;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics.AutoCloseableGraphics2D;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering.UIDefaultComponentRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering.UIDefaultRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.OneUseRunnable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.methods.IBindingMethodSource;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits.IHasBindingKey;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.ImmutableBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.methods.ImmutableBindingMethodSource;
import io.reactivex.rxjava3.observers.DisposableObserver;
import org.jetbrains.annotations.NonNls;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressThisEscapedWarning;

public class UIButtonComponent
		extends UIDefaultComponent
		implements IUIComponentCursorHandleProviderModifier {
	@NonNls
	public static final String METHOD_ON_ACTIVATE = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "method.button.activate";
	@NonNls
	public static final String METHOD_ON_ACTIVATED = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "method.button.activated";

	private static final INamespacePrefixedString METHOD_ON_ACTIVATE_LOCATION = ImmutableNamespacePrefixedString.of(getMethodOnActivate());
	private static final INamespacePrefixedString METHOD_ON_ACTIVATED_LOCATION = ImmutableNamespacePrefixedString.of(getMethodOnActivated());

	@UIMethod(METHOD_ON_ACTIVATE)
	private final IBindingMethodSource<IUIEventActivate> onActivate;
	@UIMethod(METHOD_ON_ACTIVATED)
	private final IBindingMethodSource<IUIEvent> onActivated;

	private final Set<IButtonState> buttonStates = Collections.newSetFromMap(
			MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(IButtonState.values().length).makeMap()
	);

	private final IUIRendererContainerContainer<IUIComponentRenderer<?>> rendererContainerContainer;

	private final Runnable eventTargetListenersInitializer;

	@UIComponentConstructor
	public UIButtonComponent(IUIComponentArguments arguments) {
		super(arguments);

		Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = arguments.getMappingsView();
		this.onActivate = new ImmutableBindingMethodSource<>(IUIEventActivate.class,
				Optional.ofNullable(mappings.get(getMethodOnActivateLocation())).flatMap(IUIPropertyMappingValue::getBindingKey).orElse(null));
		this.onActivated = new ImmutableBindingMethodSource<>(IUIEvent.class,
				Optional.ofNullable(mappings.get(getMethodOnActivatedLocation())).flatMap(IUIPropertyMappingValue::getBindingKey).orElse(null));

		this.rendererContainerContainer =
				UIDefaultRendererContainerContainer.ofDefault(arguments.getRendererName().orElse(null), suppressThisEscapedWarning(() -> this),
						CastUtilities.castUnchecked(DefaultRenderer.class));

		this.eventTargetListenersInitializer = new OneUseRunnable(() -> {
			addEventListener(EnumUIEventDOMType.MOUSE_ENTER_SELF.getEventType(), new UIFunctionalEventListener<IUIEventMouse>(e -> {
				if (e.getPhase() == IUIEvent.EnumPhase.AT_TARGET)
					getButtonStates().add(IButtonState.HOVERING);
			}), false);
			addEventListener(EnumUIEventDOMType.MOUSE_LEAVE_SELF.getEventType(), new UIFunctionalEventListener<IUIEventMouse>(e -> {
				if (e.getPhase() == IUIEvent.EnumPhase.AT_TARGET)
					getButtonStates().remove(IButtonState.HOVERING);
			}), false);

			addEventListener(EnumUIEventDOMType.MOUSE_DOWN.getEventType(), new UIFunctionalEventListener<IUIEventMouse>(e -> {
				if (IUIEventActivate.shouldActivate(this, e)) {
					getButtonStates().add(IButtonState.PRESSING);
					e.stopPropagation();
				}
			}), false);
			addEventListener(EnumUIEventDOMType.MOUSE_UP.getEventType(), new UIFunctionalEventListener<IUIEventMouse>(e -> {
				if (getButtonStates().remove(IButtonState.PRESSING) && getButtonStates().contains(IButtonState.HOVERING)) {
					getOnActivated().invoke(e);
					e.stopPropagation();
				}
			}), false);

			addEventListener(EnumUIEventDOMType.KEY_DOWN.getEventType(), new UIFunctionalEventListener<IUIEventKeyboard>(e -> {
				if (IUIEventActivate.shouldActivate(this, e))
					getOnActivated().invoke(e);
			}), false);
		});
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Set<EnumButtonState> getButtonStates() { return buttonStates; }

	@Override
	protected SetMultimap<INamespacePrefixedString, UIEventListenerWithParameters> getEventTargetListeners() {
		return this.eventTargetListenersInitializer.apply(super.getEventTargetListeners());
	}

	public static INamespacePrefixedString getMethodOnActivateLocation() {
		return METHOD_ON_ACTIVATE_LOCATION;
	}

	public static INamespacePrefixedString getMethodOnActivatedLocation() {
		return METHOD_ON_ACTIVATED_LOCATION;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Set<IButtonState> getButtonStates() { return buttonStates; }

	protected IBindingMethodSource<IUIEvent> getOnActivated() {
		return onActivated;
	}

	public static String getMethodOnActivate() {
		return METHOD_ON_ACTIVATE;
	}

	public static String getMethodOnActivated() {
		return METHOD_ON_ACTIVATED;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		super.initializeBindings(binderObserverSupplier);
		BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
				() -> ImmutableBinderAction.bind(getOnActivate(), getOnActivated()));
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void cleanupBindings() {
		getBinderObserverSupplierHolder().getValue().ifPresent(binderObserverSupplier ->
				BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
						() -> ImmutableBinderAction.unbind(getOnActivate(), getOnActivated()))
		);
		super.cleanupBindings();
	}

	protected IBindingMethodSource<IUIEventActivate> getOnActivate() { return onActivate; }

	@Override
	public boolean isFocusable() { return true; }

	@Override
	public Optional<Long> getCursorHandle(IUIComponentContext context) {
		return getButtonStates().contains(IButtonState.HOVERING)
				? Optional.of(EnumGLFWCursor.STANDARD_HAND_CURSOR.getHandle())
				: Optional.empty();
	}

	public enum IButtonState {
		HOVERING,
		PRESSING,
	}

	public interface IUIEventActivate extends IUIEvent {
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
			private static final INamespacePrefixedString TYPE = ImmutableNamespacePrefixedString.of(getTypeString());

			public static String getTypeString() {
				return TYPE_STRING;
			}

			public static INamespacePrefixedString getType() {
				return TYPE;
			}
		}

		class Impl
				extends UIDefaultEvent
				implements IUIEventActivate {
			static {
				UIEventRegistry.getInstance().register(StaticHolder.getType(), IUIEventActivate.class); // COMMENT custom: button will be activated
			}

			private final IUIEvent cause;

			public Impl(IUIEventTarget target, IUIEvent cause) {
				super(StaticHolder.getType(), false, true, cause.getViewContext(), target);
				this.cause = cause;
			}

			@Override
			public IUIEvent getCause() { return cause; }
		}
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

		private static final INamespacePrefixedString PROPERTY_BASE_COLOR_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyBaseColor());
		private static final INamespacePrefixedString PROPERTY_BASE_BORDER_COLOR_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyBaseBorderColor());
		private static final INamespacePrefixedString PROPERTY_HOVERING_COLOR_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyHoveringColor());
		private static final INamespacePrefixedString PROPERTY_HOVERING_BORDER_COLOR_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyHoveringBorderColor());
		private static final INamespacePrefixedString PROPERTY_PRESSED_COLOR_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyPressedColor());
		private static final INamespacePrefixedString PROPERTY_PRESSED_BORDER_COLOR_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyPressedBorderColor());

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

			Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = arguments.getMappingsView();
			this.baseColor = IUIPropertyMappingValue.createBindingField(Color.class, Color.DARK_GRAY,
					mappings.get(getPropertyBaseColorLocation()));
			this.baseBorderColor = IUIPropertyMappingValue.createBindingField(Color.class, Color.DARK_GRAY,
					mappings.get(getPropertyBaseBorderColorLocation()));
			this.hoveringColor = IUIPropertyMappingValue.createBindingField(Color.class, Color.GRAY,
					mappings.get(getPropertyHoveringColorLocation()));
			this.hoveringBorderColor = IUIPropertyMappingValue.createBindingField(Color.class, Color.GRAY,
					mappings.get(getPropertyHoveringBorderColorLocation()));
			this.pressedColor = IUIPropertyMappingValue.createBindingField(Color.class, Color.LIGHT_GRAY,
					mappings.get(getPropertyPressedColorLocation()));
			this.pressedBorderColor = IUIPropertyMappingValue.createBindingField(Color.class, Color.LIGHT_GRAY,
					mappings.get(getPropertyPressedBorderColorLocation()));
		}

		public static INamespacePrefixedString getPropertyBaseColorLocation() {
			return PROPERTY_BASE_COLOR_LOCATION;
		}

		public static INamespacePrefixedString getPropertyBaseBorderColorLocation() {
			return PROPERTY_BASE_BORDER_COLOR_LOCATION;
		}

		public static INamespacePrefixedString getPropertyHoveringColorLocation() {
			return PROPERTY_HOVERING_COLOR_LOCATION;
		}

		public static INamespacePrefixedString getPropertyHoveringBorderColorLocation() {
			return PROPERTY_HOVERING_BORDER_COLOR_LOCATION;
		}

		public static INamespacePrefixedString getPropertyPressedColorLocation() {
			return PROPERTY_PRESSED_COLOR_LOCATION;
		}

		public static INamespacePrefixedString getPropertyPressedBorderColorLocation() {
			return PROPERTY_PRESSED_BORDER_COLOR_LOCATION;
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
		public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
			super.initializeBindings(binderObserverSupplier);
			BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
					() -> ImmutableBinderAction.bind(
							getBaseColor(), getBaseBorderColor(),
							getHoveringColor(), getHoveringBorderColor(),
							getPressedColor(), getPressedBorderColor()
					));
		}

		@Override
		@OverridingMethodsMustInvokeSuper
		public void cleanupBindings() {
			getBinderObserverSupplierHolder().getValue().ifPresent(binderObserverSupplier ->
					BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
							() -> ImmutableBinderAction.unbind(
									getBaseColor(), getBaseBorderColor(),
									getHoveringColor(), getHoveringBorderColor(),
									getPressedColor(), getPressedBorderColor()
							)));
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
