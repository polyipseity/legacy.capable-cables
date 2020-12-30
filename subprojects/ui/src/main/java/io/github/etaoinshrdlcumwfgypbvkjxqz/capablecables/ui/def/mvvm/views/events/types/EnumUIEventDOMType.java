package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.events.types;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.events.IUIEventType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableIdentifier;
import org.jetbrains.annotations.NonNls;

public enum EnumUIEventDOMType
		implements IUIEventType {
	LOAD(StaticHolder.getDefaultPrefix() + "load"),
	UNLOAD(StaticHolder.getDefaultPrefix() + "unload"),
	ABORT(StaticHolder.getDefaultPrefix() + "abort"),
	ERROR(StaticHolder.getDefaultPrefix() + "error"),
	SELECT(StaticHolder.getDefaultPrefix() + "select"),
	FOCUS_OUT_POST(StaticHolder.getDefaultPrefix() + "blur"),
	FOCUS_IN_POST(StaticHolder.getDefaultPrefix() + "focus"),
	@SuppressWarnings("SpellCheckingInspection")
	FOCUS_IN_PRE(StaticHolder.getDefaultPrefix() + "focusin"),
	@SuppressWarnings("SpellCheckingInspection")
	FOCUS_OUT_PRE(StaticHolder.getDefaultPrefix() + "focusout"),
	@SuppressWarnings("SpellCheckingInspection")
	CLICK_AUXILIARY(StaticHolder.getDefaultPrefix() + "auxclick"),
	CLICK(StaticHolder.getDefaultPrefix() + "click"),
	@SuppressWarnings("SpellCheckingInspection")
	CLICK_DOUBLE(StaticHolder.getDefaultPrefix() + "dblclick"),
	@SuppressWarnings("SpellCheckingInspection")
	MOUSE_DOWN(StaticHolder.getDefaultPrefix() + "mousedown"),
	@SuppressWarnings("SpellCheckingInspection")
	MOUSE_ENTER(StaticHolder.getDefaultPrefix() + "mouseenter"),
	@SuppressWarnings("SpellCheckingInspection")
	MOUSE_LEAVE(StaticHolder.getDefaultPrefix() + "mouseleave"),
	@SuppressWarnings("SpellCheckingInspection")
	MOUSE_MOVE(StaticHolder.getDefaultPrefix() + "mousemove"),
	@SuppressWarnings("SpellCheckingInspection")
	MOUSE_LEAVE_SELF(StaticHolder.getDefaultPrefix() + "mouseout"),
	MOUSE_ENTER_SELF(StaticHolder.getDefaultPrefix() + "mouseover"),
	@SuppressWarnings("SpellCheckingInspection")
	MOUSE_UP(StaticHolder.getDefaultPrefix() + "mouseup"),
	WHEEL(StaticHolder.getDefaultPrefix() + "wheel"),
	@SuppressWarnings("SpellCheckingInspection")
	INPUT_PRE(StaticHolder.getDefaultPrefix() + "beforeinput"),
	INPUT_POST(StaticHolder.getDefaultPrefix() + "input"),
	@SuppressWarnings("SpellCheckingInspection")
	KEY_DOWN(StaticHolder.getDefaultPrefix() + "keydown"),
	@SuppressWarnings("SpellCheckingInspection")
	KEY_UP(StaticHolder.getDefaultPrefix() + "keyup"),
	@SuppressWarnings("SpellCheckingInspection")
	COMPOSITION_START(StaticHolder.getDefaultPrefix() + "compositionstart"),
	@SuppressWarnings("SpellCheckingInspection")
	COMPOSITION_UPDATE(StaticHolder.getDefaultPrefix() + "compositionupdate"),
	@SuppressWarnings("SpellCheckingInspection")
	COMPOSITION_END(StaticHolder.getDefaultPrefix() + "compositionend"),
	;

	@NonNls
	private final String eventTypeString;
	private final IIdentifier eventType;

	EnumUIEventDOMType(@NonNls CharSequence eventTypeString) {
		this.eventType = ImmutableIdentifier.ofInterning(this.eventTypeString = eventTypeString.toString()); // COMMENT Wow!  What a great piece of readable code.
	}

	@Override
	@NonNls
	public String getEventTypeString() { return eventTypeString; }

	@Override
	public IIdentifier getEventType() { return eventType; }
}
