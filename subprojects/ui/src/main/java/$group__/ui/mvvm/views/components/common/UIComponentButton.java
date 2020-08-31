package $group__.ui.mvvm.views.components.common;

import $group__.ui.core.mvvm.binding.IBindingMethod;
import $group__.ui.core.mvvm.structures.IAffineTransformStack;
import $group__.ui.core.mvvm.structures.IUIPropertyMappingValue;
import $group__.ui.core.mvvm.views.components.extensions.cursors.IUIComponentCursorHandleProvider;
import $group__.ui.core.mvvm.views.events.IUIEvent;
import $group__.ui.core.mvvm.views.events.IUIEventKeyboard;
import $group__.ui.core.mvvm.views.events.IUIEventMouse;
import $group__.ui.core.mvvm.views.events.IUIEventTarget;
import $group__.ui.core.parsers.binding.UIMethod;
import $group__.ui.core.parsers.components.UIComponentConstructor;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.ui.events.ui.UIEvent;
import $group__.ui.events.ui.UIEventListener;
import $group__.ui.events.ui.UIEventUtilities;
import $group__.ui.mvvm.binding.BindingMethodSource;
import $group__.ui.mvvm.views.components.UIComponentContainer;
import $group__.ui.mvvm.views.events.ui.UIEventKeyboard;
import $group__.ui.mvvm.views.events.ui.UIEventMouse;
import $group__.ui.structures.EnumCursor;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.NamespacePrefixedString;

import java.awt.geom.Point2D;
import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class UIComponentButton
		extends UIComponentContainer
		implements IUIComponentCursorHandleProvider {
	public static final String METHOD_ON_ACTIVATE = INamespacePrefixedString.DEFAULT_PREFIX + "button.methods.activate";
	public static final String METHOD_ON_ACTIVATED = INamespacePrefixedString.DEFAULT_PREFIX + "button.methods.activated";

	public static final INamespacePrefixedString METHOD_ON_ACTIVATE_LOCATION = new NamespacePrefixedString(METHOD_ON_ACTIVATE);
	public static final INamespacePrefixedString METHOD_ON_ACTIVATED_LOCATION = new NamespacePrefixedString(METHOD_ON_ACTIVATED);

	@UIMethod(METHOD_ON_ACTIVATE)
	protected final IBindingMethod.Source<IUIEventActivate> methodOnActivate;
	@UIMethod(METHOD_ON_ACTIVATED)
	protected final IBindingMethod.Source<IUIEvent> methodOnActivated;

	protected final Set<IButtonState> buttonStates = EnumSet.noneOf(IButtonState.class);

	@SuppressWarnings("OverridableMethodCallDuringObjectConstruction")
	@UIComponentConstructor(type = UIComponentConstructor.ConstructorType.MAPPING__SHAPE_DESCRIPTOR)
	public UIComponentButton(Map<INamespacePrefixedString, IUIPropertyMappingValue> mapping, IShapeDescriptor<?> shapeDescriptor) {
		super(mapping, shapeDescriptor);

		this.methodOnActivate = new BindingMethodSource<>(IUIEventActivate.class,
				Optional.ofNullable(this.mapping.get(METHOD_ON_ACTIVATE_LOCATION)).flatMap(IUIPropertyMappingValue::getBindingKey).orElse(null));
		this.methodOnActivated = new BindingMethodSource<>(IUIEvent.class,
				Optional.ofNullable(this.mapping.get(METHOD_ON_ACTIVATED_LOCATION)).flatMap(IUIPropertyMappingValue::getBindingKey).orElse(null));

		addEventListener(UIEventMouse.TYPE_MOUSE_ENTER_SELF, new UIEventListener.Functional<IUIEventMouse>(e -> {
			if (e.getPhase() == IUIEvent.EnumPhase.AT_TARGET)
				getButtonStates().add(IButtonState.HOVERING);
		}), false);
		addEventListener(UIEventMouse.TYPE_MOUSE_LEAVE_SELF, new UIEventListener.Functional<IUIEventMouse>(e -> {
			if (e.getPhase() == IUIEvent.EnumPhase.AT_TARGET)
				getButtonStates().remove(IButtonState.HOVERING);
		}), false);

		addEventListener(UIEventMouse.TYPE_MOUSE_DOWN, new UIEventListener.Functional<IUIEventMouse>(e -> {
			if (IUIEventActivate.shouldActivate(this, e)) {
				getButtonStates().add(IButtonState.PRESSING);
				e.stopPropagation();
			}
		}), false);
		addEventListener(UIEventMouse.TYPE_MOUSE_UP, new UIEventListener.Functional<IUIEventMouse>(e -> {
			if (getButtonStates().remove(IButtonState.PRESSING) && getButtonStates().contains(IButtonState.HOVERING)) {
				getMethodOnActivated().invoke(e);
				e.stopPropagation();
			}
		}), false);

		addEventListener(UIEventKeyboard.TYPE_KEY_DOWN, new UIEventListener.Functional<IUIEventKeyboard>(e -> {
			if (IUIEventActivate.shouldActivate(this, e))
				getMethodOnActivated().invoke(e);
		}), false);
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Set<IButtonState> getButtonStates() { return buttonStates; }

	protected IBindingMethod.Source<IUIEvent> getMethodOnActivated() {
		return methodOnActivated;
	}

	protected IBindingMethod.Source<IUIEventActivate> getMethodOnActivate() { return methodOnActivate; }

	@Override
	public boolean isFocusable() { return true; }

	@Override
	public Optional<? extends Long> getCursorHandle(IAffineTransformStack stack, Point2D cursorPosition) {
		return getButtonStates().contains(IButtonState.HOVERING)
				? Optional.of(EnumCursor.STANDARD_HAND_CURSOR.getHandle())
				: Optional.empty();
	}

	public enum IButtonState {
		HOVERING,
		PRESSING,
	}

	public interface IUIEventActivate extends IUIEvent {
		String TYPE_STRING = INamespacePrefixedString.DEFAULT_PREFIX + "button.activated";
		INamespacePrefixedString TYPE = new NamespacePrefixedString(TYPE_STRING);

		static boolean shouldActivate(UIComponentButton self, IUIEvent event) {
			return UIEventUtilities.dispatchEvent(new IUIEventActivate.Impl((Functional) e -> {
				self.getMethodOnActivate().invoke((IUIEventActivate) e);
				return !event.isDefaultPrevented();
			}, event));
		}

		IUIEvent getCause();

		class Impl
				extends UIEvent
				implements IUIEventActivate {
			static {
				UIEventUtilities.RegUIEvent.INSTANCE.register(TYPE, IUIEventActivate.class); // COMMENT custom: button will be activated
			}

			protected final IUIEvent cause;

			public Impl(IUIEventTarget target, IUIEvent cause) {
				super(TYPE, false, true, target);
				this.cause = cause;
			}

			@Override
			public IUIEvent getCause() { return cause; }
		}
	}
}
