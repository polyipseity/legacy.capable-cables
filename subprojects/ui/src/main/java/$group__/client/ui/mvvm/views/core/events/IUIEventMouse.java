package $group__.client.ui.mvvm.views.core.events;

import $group__.client.ui.mvvm.core.structures.IUIDataMouseButtonClick;
import $group__.utilities.NamespaceUtilities;
import net.minecraft.util.ResourceLocation;

import java.util.Optional;

public interface IUIEventMouse extends IUIEvent {
	@SuppressWarnings({"SpellCheckingInspection", "RedundantSuppression"})
	String
			TYPE_CLICK_STRING = NamespaceUtilities.NAMESPACE_DEFAULT + NamespaceUtilities.NAMESPACE_SEPARATOR_DEFAULT + "click",
			TYPE_CLICK_DOUBLE_STRING = NamespaceUtilities.NAMESPACE_DEFAULT + NamespaceUtilities.NAMESPACE_SEPARATOR_DEFAULT + "dblclick",
			TYPE_MOUSE_DOWN_STRING = NamespaceUtilities.NAMESPACE_DEFAULT + NamespaceUtilities.NAMESPACE_SEPARATOR_DEFAULT + "mousedown",
			TYPE_MOUSE_ENTER_STRING = NamespaceUtilities.NAMESPACE_DEFAULT + NamespaceUtilities.NAMESPACE_SEPARATOR_DEFAULT + "mouseenter",
			TYPE_MOUSE_LEAVE_STRING = NamespaceUtilities.NAMESPACE_DEFAULT + NamespaceUtilities.NAMESPACE_SEPARATOR_DEFAULT + "mouseleave",
			TYPE_MOUSE_MOVE_STRING = NamespaceUtilities.NAMESPACE_DEFAULT + NamespaceUtilities.NAMESPACE_SEPARATOR_DEFAULT + "mousemove",
			TYPE_MOUSE_LEAVE_SELF_OR_CHILD_STRING = NamespaceUtilities.NAMESPACE_DEFAULT + NamespaceUtilities.NAMESPACE_SEPARATOR_DEFAULT + "mouseout",
			TYPE_MOUSE_ENTER_SELF_OR_CHILD_STRING = NamespaceUtilities.NAMESPACE_DEFAULT + NamespaceUtilities.NAMESPACE_SEPARATOR_DEFAULT + "mouseover",
			TYPE_MOUSE_UP_STRING = NamespaceUtilities.NAMESPACE_DEFAULT + NamespaceUtilities.NAMESPACE_SEPARATOR_DEFAULT + "mouseup";
	ResourceLocation
			TYPE_CLICK = new ResourceLocation(TYPE_CLICK_STRING),
			TYPE_CLICK_DOUBLE = new ResourceLocation(TYPE_CLICK_DOUBLE_STRING),
			TYPE_MOUSE_DOWN = new ResourceLocation(TYPE_MOUSE_DOWN_STRING),
			TYPE_MOUSE_ENTER = new ResourceLocation(TYPE_MOUSE_ENTER_STRING),
			TYPE_MOUSE_LEAVE = new ResourceLocation(TYPE_MOUSE_LEAVE_STRING),
			TYPE_MOUSE_MOVE = new ResourceLocation(TYPE_MOUSE_MOVE_STRING),
			TYPE_MOUSE_LEAVE_SELF_OR_CHILD = new ResourceLocation(TYPE_MOUSE_LEAVE_SELF_OR_CHILD_STRING),
			TYPE_MOUSE_ENTER_SELF_OR_CHILD = new ResourceLocation(TYPE_MOUSE_ENTER_SELF_OR_CHILD_STRING),
			TYPE_MOUSE_UP = new ResourceLocation(TYPE_MOUSE_UP_STRING);

	IUIDataMouseButtonClick getData();

	Optional<IUIEventTarget> getRelatedTarget();
}
