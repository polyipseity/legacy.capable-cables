package $group__.ui.core.mvvm.views.events.types;

import $group__.ui.core.mvvm.views.events.IUIEventType;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.NamespacePrefixedString;

public enum EnumUIEventDOMType
		implements IUIEventType {
	LOAD(INamespacePrefixedString.DEFAULT_PREFIX + "load"),
	UNLOAD(INamespacePrefixedString.DEFAULT_PREFIX + "unload"),
	ABORT(INamespacePrefixedString.DEFAULT_PREFIX + "abort"),
	ERROR(INamespacePrefixedString.DEFAULT_PREFIX + "error"),
	SELECT(INamespacePrefixedString.DEFAULT_PREFIX + "select"),
	FOCUS_OUT_POST(INamespacePrefixedString.DEFAULT_PREFIX + "blur"),
	FOCUS_IN_POST(INamespacePrefixedString.DEFAULT_PREFIX + "focus"),
	@SuppressWarnings("SpellCheckingInspection")
	FOCUS_IN_PRE(INamespacePrefixedString.DEFAULT_PREFIX + "focusin"),
	@SuppressWarnings("SpellCheckingInspection")
	FOCUS_OUT_PRE(INamespacePrefixedString.DEFAULT_PREFIX + "focusout"),
	@SuppressWarnings("SpellCheckingInspection")
	CLICK_AUXILIARY(INamespacePrefixedString.DEFAULT_PREFIX + "auxclick"),
	CLICK(INamespacePrefixedString.DEFAULT_PREFIX + "click"),
	@SuppressWarnings("SpellCheckingInspection")
	CLICK_DOUBLE(INamespacePrefixedString.DEFAULT_PREFIX + "dblclick"),
	@SuppressWarnings("SpellCheckingInspection")
	MOUSE_DOWN(INamespacePrefixedString.DEFAULT_PREFIX + "mousedown"),
	@SuppressWarnings("SpellCheckingInspection")
	MOUSE_ENTER(INamespacePrefixedString.DEFAULT_PREFIX + "mouseenter"),
	@SuppressWarnings("SpellCheckingInspection")
	MOUSE_LEAVE(INamespacePrefixedString.DEFAULT_PREFIX + "mouseleave"),
	@SuppressWarnings("SpellCheckingInspection")
	MOUSE_MOVE(INamespacePrefixedString.DEFAULT_PREFIX + "mousemove"),
	@SuppressWarnings("SpellCheckingInspection")
	MOUSE_LEAVE_SELF(INamespacePrefixedString.DEFAULT_PREFIX + "mouseout"),
	MOUSE_ENTER_SELF(INamespacePrefixedString.DEFAULT_PREFIX + "mouseover"),
	@SuppressWarnings("SpellCheckingInspection")
	MOUSE_UP(INamespacePrefixedString.DEFAULT_PREFIX + "mouseup"),
	WHEEL(INamespacePrefixedString.DEFAULT_PREFIX + "wheel"),
	@SuppressWarnings("SpellCheckingInspection")
	UPDATE_PRE(INamespacePrefixedString.DEFAULT_PREFIX + "beforeinput"),
	UPDATE_POST(INamespacePrefixedString.DEFAULT_PREFIX + "input"),
	@SuppressWarnings("SpellCheckingInspection")
	KEY_DOWN(INamespacePrefixedString.DEFAULT_PREFIX + "keydown"),
	@SuppressWarnings("SpellCheckingInspection")
	KEY_UP(INamespacePrefixedString.DEFAULT_PREFIX + "keyup"),
	@SuppressWarnings("SpellCheckingInspection")
	COMPOSITION_START(INamespacePrefixedString.DEFAULT_PREFIX + "compositionstart"),
	@SuppressWarnings("SpellCheckingInspection")
	COMPOSITION_UPDATE(INamespacePrefixedString.DEFAULT_PREFIX + "compositionupdate"),
	@SuppressWarnings("SpellCheckingInspection")
	COMPOSITION_END(INamespacePrefixedString.DEFAULT_PREFIX + "compositionend"),
	;

	protected final String eventTypeString;
	protected final INamespacePrefixedString eventType;

	EnumUIEventDOMType(String eventTypeString) {
		this.eventTypeString = eventTypeString;
		this.eventType = new NamespacePrefixedString(this.eventTypeString);
	}

	@Override
	public String getEventTypeString() { return eventTypeString; }

	@Override
	public INamespacePrefixedString getEventType() { return eventType; }
}
