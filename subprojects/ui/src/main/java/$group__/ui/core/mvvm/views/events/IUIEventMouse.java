package $group__.ui.core.mvvm.views.events;

import $group__.ui.core.structures.IUIDataMouseButtonClick;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.NamespacePrefixedString;

import java.util.Optional;

public interface IUIEventMouse extends IUIEvent {
	@SuppressWarnings({"SpellCheckingInspection", "RedundantSuppression"})
	String
			TYPE_CLICK_STRING = INamespacePrefixedString.DEFAULT_PREFIX + "click",
			TYPE_CLICK_DOUBLE_STRING = INamespacePrefixedString.DEFAULT_PREFIX + "dblclick",
			TYPE_MOUSE_DOWN_STRING = INamespacePrefixedString.DEFAULT_PREFIX + "mousedown",
			TYPE_MOUSE_ENTER_STRING = INamespacePrefixedString.DEFAULT_PREFIX + "mouseenter",
			TYPE_MOUSE_LEAVE_STRING = INamespacePrefixedString.DEFAULT_PREFIX + "mouseleave",
			TYPE_MOUSE_MOVE_STRING = INamespacePrefixedString.DEFAULT_PREFIX + "mousemove",
			TYPE_MOUSE_LEAVE_SELF_STRING = INamespacePrefixedString.DEFAULT_PREFIX + "mouseout",
			TYPE_MOUSE_ENTER_SELF_STRING = INamespacePrefixedString.DEFAULT_PREFIX + "mouseover",
			TYPE_MOUSE_UP_STRING = INamespacePrefixedString.DEFAULT_PREFIX + "mouseup";
	INamespacePrefixedString
			TYPE_CLICK = new NamespacePrefixedString(TYPE_CLICK_STRING),
			TYPE_CLICK_DOUBLE = new NamespacePrefixedString(TYPE_CLICK_DOUBLE_STRING),
			TYPE_MOUSE_DOWN = new NamespacePrefixedString(TYPE_MOUSE_DOWN_STRING),
			TYPE_MOUSE_ENTER = new NamespacePrefixedString(TYPE_MOUSE_ENTER_STRING),
			TYPE_MOUSE_LEAVE = new NamespacePrefixedString(TYPE_MOUSE_LEAVE_STRING),
			TYPE_MOUSE_MOVE = new NamespacePrefixedString(TYPE_MOUSE_MOVE_STRING),
			TYPE_MOUSE_LEAVE_SELF = new NamespacePrefixedString(TYPE_MOUSE_LEAVE_SELF_STRING),
			TYPE_MOUSE_ENTER_SELF = new NamespacePrefixedString(TYPE_MOUSE_ENTER_SELF_STRING),
			TYPE_MOUSE_UP = new NamespacePrefixedString(TYPE_MOUSE_UP_STRING);

	IUIDataMouseButtonClick getData();

	Optional<IUIEventTarget> getRelatedTarget();
}
