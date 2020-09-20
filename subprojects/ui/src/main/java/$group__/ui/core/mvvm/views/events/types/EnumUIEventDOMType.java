package $group__.ui.core.mvvm.views.events.types;

import $group__.ui.core.mvvm.views.events.IUIEventType;
import $group__.utilities.structures.INamespacePrefixedString;
import $group__.utilities.structures.ImmutableNamespacePrefixedString;
import org.jetbrains.annotations.NonNls;

public enum EnumUIEventDOMType
		implements IUIEventType {
	LOAD(INamespacePrefixedString.StaticHolder.getDefaultPrefix() + "load"),
	UNLOAD(INamespacePrefixedString.StaticHolder.getDefaultPrefix() + "unload"),
	ABORT(INamespacePrefixedString.StaticHolder.getDefaultPrefix() + "abort"),
	ERROR(INamespacePrefixedString.StaticHolder.getDefaultPrefix() + "error"),
	SELECT(INamespacePrefixedString.StaticHolder.getDefaultPrefix() + "select"),
	FOCUS_OUT_POST(INamespacePrefixedString.StaticHolder.getDefaultPrefix() + "blur"),
	FOCUS_IN_POST(INamespacePrefixedString.StaticHolder.getDefaultPrefix() + "focus"),
	@SuppressWarnings("SpellCheckingInspection")
	FOCUS_IN_PRE(INamespacePrefixedString.StaticHolder.getDefaultPrefix() + "focusin"),
	@SuppressWarnings("SpellCheckingInspection")
	FOCUS_OUT_PRE(INamespacePrefixedString.StaticHolder.getDefaultPrefix() + "focusout"),
	@SuppressWarnings("SpellCheckingInspection")
	CLICK_AUXILIARY(INamespacePrefixedString.StaticHolder.getDefaultPrefix() + "auxclick"),
	CLICK(INamespacePrefixedString.StaticHolder.getDefaultPrefix() + "click"),
	@SuppressWarnings("SpellCheckingInspection")
	CLICK_DOUBLE(INamespacePrefixedString.StaticHolder.getDefaultPrefix() + "dblclick"),
	@SuppressWarnings("SpellCheckingInspection")
	MOUSE_DOWN(INamespacePrefixedString.StaticHolder.getDefaultPrefix() + "mousedown"),
	@SuppressWarnings("SpellCheckingInspection")
	MOUSE_ENTER(INamespacePrefixedString.StaticHolder.getDefaultPrefix() + "mouseenter"),
	@SuppressWarnings("SpellCheckingInspection")
	MOUSE_LEAVE(INamespacePrefixedString.StaticHolder.getDefaultPrefix() + "mouseleave"),
	@SuppressWarnings("SpellCheckingInspection")
	MOUSE_MOVE(INamespacePrefixedString.StaticHolder.getDefaultPrefix() + "mousemove"),
	@SuppressWarnings("SpellCheckingInspection")
	MOUSE_LEAVE_SELF(INamespacePrefixedString.StaticHolder.getDefaultPrefix() + "mouseout"),
	MOUSE_ENTER_SELF(INamespacePrefixedString.StaticHolder.getDefaultPrefix() + "mouseover"),
	@SuppressWarnings("SpellCheckingInspection")
	MOUSE_UP(INamespacePrefixedString.StaticHolder.getDefaultPrefix() + "mouseup"),
	WHEEL(INamespacePrefixedString.StaticHolder.getDefaultPrefix() + "wheel"),
	@SuppressWarnings("SpellCheckingInspection")
	UPDATE_PRE(INamespacePrefixedString.StaticHolder.getDefaultPrefix() + "beforeinput"),
	UPDATE_POST(INamespacePrefixedString.StaticHolder.getDefaultPrefix() + "input"),
	@SuppressWarnings("SpellCheckingInspection")
	KEY_DOWN(INamespacePrefixedString.StaticHolder.getDefaultPrefix() + "keydown"),
	@SuppressWarnings("SpellCheckingInspection")
	KEY_UP(INamespacePrefixedString.StaticHolder.getDefaultPrefix() + "keyup"),
	@SuppressWarnings("SpellCheckingInspection")
	COMPOSITION_START(INamespacePrefixedString.StaticHolder.getDefaultPrefix() + "compositionstart"),
	@SuppressWarnings("SpellCheckingInspection")
	COMPOSITION_UPDATE(INamespacePrefixedString.StaticHolder.getDefaultPrefix() + "compositionupdate"),
	@SuppressWarnings("SpellCheckingInspection")
	COMPOSITION_END(INamespacePrefixedString.StaticHolder.getDefaultPrefix() + "compositionend"),
	;

	@NonNls
	protected final String eventTypeString;
	protected final INamespacePrefixedString eventType;

	EnumUIEventDOMType(@NonNls String eventTypeString) {
		this.eventTypeString = eventTypeString;
		this.eventType = new ImmutableNamespacePrefixedString(this.eventTypeString);
	}

	@Override
	@NonNls
	public String getEventTypeString() { return eventTypeString; }

	@Override
	public INamespacePrefixedString getEventType() { return eventType; }
}
