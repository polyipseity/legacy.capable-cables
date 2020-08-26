package $group__.ui.mvvm.views.components.common;

import $group__.ui.core.mvvm.binding.IBindingMethod;
import $group__.ui.core.mvvm.structures.IAffineTransformStack;
import $group__.ui.core.mvvm.structures.IUIPropertyMappingValue;
import $group__.ui.core.mvvm.views.components.extensions.cursors.IUIComponentCursorHandleProvider;
import $group__.ui.core.mvvm.views.events.IUIEvent;
import $group__.ui.core.mvvm.views.events.IUIEventKeyboard;
import $group__.ui.core.mvvm.views.events.IUIEventMouse;
import $group__.ui.core.parsers.binding.UIMethod;
import $group__.ui.core.parsers.components.UIComponentConstructor;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.ui.events.ui.UIEventListener;
import $group__.ui.mvvm.binding.BindingMethodSource;
import $group__.ui.mvvm.views.components.UIComponent;
import $group__.ui.mvvm.views.events.ui.UIEventKeyboard;
import $group__.ui.mvvm.views.events.ui.UIEventMouse;
import $group__.ui.structures.EnumCursor;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.NamespacePrefixedString;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;
import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;

public class UIComponentButton
		extends UIComponent
		implements IUIComponentCursorHandleProvider {
	public static final String METHOD_ON_ACTIVATED = INamespacePrefixedString.DEFAULT_PREFIX + "button.methods.activated";

	public static final INamespacePrefixedString METHOD_ON_ACTIVATED_LOCATION = new NamespacePrefixedString(METHOD_ON_ACTIVATED);

	@UIMethod(METHOD_ON_ACTIVATED)
	protected final IBindingMethod.ISource<IUIEvent> methodOnActivated;

	protected final EnumSet<IButtonState> buttonStates = EnumSet.noneOf(IButtonState.class);

	@SuppressWarnings("OverridableMethodCallDuringObjectConstruction")
	@UIComponentConstructor(type = UIComponentConstructor.ConstructorType.SHAPE_DESCRIPTOR__MAPPING)
	public UIComponentButton(IShapeDescriptor<?> shapeDescriptor, Map<INamespacePrefixedString, IUIPropertyMappingValue> mapping) {
		super(shapeDescriptor, mapping);

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
			getButtonStates().add(IButtonState.PRESSING);
			e.stopPropagation();
		}), false);
		addEventListener(UIEventMouse.TYPE_MOUSE_UP, new UIEventListener.Functional<IUIEventMouse>(e -> {
			getButtonStates().remove(IButtonState.PRESSING);
			e.stopPropagation();
		}), false);

		addEventListener(UIEventMouse.TYPE_CLICK, new UIEventListener.Functional<IUIEventMouse>(e -> {
			getMethodOnActivated().invoke(e);
			e.stopPropagation();
		}), false);

		addEventListener(UIEventKeyboard.TYPE_KEY_DOWN, new UIEventListener.Functional<IUIEventKeyboard>(e -> {
			getMethodOnActivated().invoke(e);
			e.stopPropagation();
		}), false);
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected EnumSet<IButtonState> getButtonStates() { return buttonStates; }

	protected IBindingMethod.ISource<IUIEvent> getMethodOnActivated() {
		return methodOnActivated;
	}

	@Override
	public boolean isFocusable() { return true; }

	@Override
	public Optional<Long> getCursorHandle(IAffineTransformStack stack, Point2D cursorPosition) {
		if (getButtonStates().contains(IButtonState.HOVERING))
			return Optional.of(EnumCursor.STANDARD_HAND_CURSOR.getHandle());
		return Optional.empty();
	}

	@OnlyIn(Dist.CLIENT)
	public enum IButtonState {
		HOVERING,
		PRESSING,
	}
}
