package $group__.client.ui.mvvm.core.views.events;

import $group__.client.ui.core.structures.IUIDataMouseButtonClick;
import $group__.utilities.NamespaceUtilities;
import net.minecraft.util.ResourceLocation;

import java.util.Optional;

public interface IUIEventMouse extends IUIEvent {
	@SuppressWarnings({"SpellCheckingInspection", "RedundantSuppression"})
	String
			TYPE_CLICK_STRING = NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT_PREFIX + "click",
			TYPE_CLICK_DOUBLE_STRING = NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT_PREFIX + "dblclick",
			TYPE_MOUSE_DOWN_STRING = NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT_PREFIX + "mousedown",
			TYPE_MOUSE_ENTER_STRING = NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT_PREFIX + "mouseenter",
			TYPE_MOUSE_LEAVE_STRING = NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT_PREFIX + "mouseleave",
			TYPE_MOUSE_MOVE_STRING = NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT_PREFIX + "mousemove",
			TYPE_MOUSE_LEAVE_SELF_STRING = NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT_PREFIX + "mouseout",
			TYPE_MOUSE_ENTER_SELF_STRING = NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT_PREFIX + "mouseover",
			TYPE_MOUSE_UP_STRING = NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT_PREFIX + "mouseup";
	ResourceLocation
			TYPE_CLICK = new ResourceLocation(TYPE_CLICK_STRING),
			TYPE_CLICK_DOUBLE = new ResourceLocation(TYPE_CLICK_DOUBLE_STRING),
			TYPE_MOUSE_DOWN = new ResourceLocation(TYPE_MOUSE_DOWN_STRING),
			TYPE_MOUSE_ENTER = new ResourceLocation(TYPE_MOUSE_ENTER_STRING),
			TYPE_MOUSE_LEAVE = new ResourceLocation(TYPE_MOUSE_LEAVE_STRING),
			TYPE_MOUSE_MOVE = new ResourceLocation(TYPE_MOUSE_MOVE_STRING),
			TYPE_MOUSE_LEAVE_SELF = new ResourceLocation(TYPE_MOUSE_LEAVE_SELF_STRING),
			TYPE_MOUSE_ENTER_SELF = new ResourceLocation(TYPE_MOUSE_ENTER_SELF_STRING),
			TYPE_MOUSE_UP = new ResourceLocation(TYPE_MOUSE_UP_STRING);

	IUIDataMouseButtonClick getData();

	Optional<IUIEventTarget> getRelatedTarget();
}
