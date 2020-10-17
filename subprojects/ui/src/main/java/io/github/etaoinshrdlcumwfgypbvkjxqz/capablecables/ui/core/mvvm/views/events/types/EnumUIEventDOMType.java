package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.types;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import org.jetbrains.annotations.NonNls;

public enum EnumUIEventDOMType
		implements IUIEventType {
	LOAD(INamespacePrefixedString.StaticHolder.DEFAULT_PREFIX + "load"),
	UNLOAD(INamespacePrefixedString.StaticHolder.DEFAULT_PREFIX + "unload"),
	ABORT(INamespacePrefixedString.StaticHolder.DEFAULT_PREFIX + "abort"),
	ERROR(INamespacePrefixedString.StaticHolder.DEFAULT_PREFIX + "error"),
	SELECT(INamespacePrefixedString.StaticHolder.DEFAULT_PREFIX + "select"),
	FOCUS_OUT_POST(INamespacePrefixedString.StaticHolder.DEFAULT_PREFIX + "blur"),
	FOCUS_IN_POST(INamespacePrefixedString.StaticHolder.DEFAULT_PREFIX + "focus"),
	@SuppressWarnings("SpellCheckingInspection")
	FOCUS_IN_PRE(INamespacePrefixedString.StaticHolder.DEFAULT_PREFIX + "focusin"),
	@SuppressWarnings("SpellCheckingInspection")
	FOCUS_OUT_PRE(INamespacePrefixedString.StaticHolder.DEFAULT_PREFIX + "focusout"),
	@SuppressWarnings("SpellCheckingInspection")
	CLICK_AUXILIARY(INamespacePrefixedString.StaticHolder.DEFAULT_PREFIX + "auxclick"),
	CLICK(INamespacePrefixedString.StaticHolder.DEFAULT_PREFIX + "click"),
	@SuppressWarnings("SpellCheckingInspection")
	CLICK_DOUBLE(INamespacePrefixedString.StaticHolder.DEFAULT_PREFIX + "dblclick"),
	@SuppressWarnings("SpellCheckingInspection")
	MOUSE_DOWN(INamespacePrefixedString.StaticHolder.DEFAULT_PREFIX + "mousedown"),
	@SuppressWarnings("SpellCheckingInspection")
	MOUSE_ENTER(INamespacePrefixedString.StaticHolder.DEFAULT_PREFIX + "mouseenter"),
	@SuppressWarnings("SpellCheckingInspection")
	MOUSE_LEAVE(INamespacePrefixedString.StaticHolder.DEFAULT_PREFIX + "mouseleave"),
	@SuppressWarnings("SpellCheckingInspection")
	MOUSE_MOVE(INamespacePrefixedString.StaticHolder.DEFAULT_PREFIX + "mousemove"),
	@SuppressWarnings("SpellCheckingInspection")
	MOUSE_LEAVE_SELF(INamespacePrefixedString.StaticHolder.DEFAULT_PREFIX + "mouseout"),
	MOUSE_ENTER_SELF(INamespacePrefixedString.StaticHolder.DEFAULT_PREFIX + "mouseover"),
	@SuppressWarnings("SpellCheckingInspection")
	MOUSE_UP(INamespacePrefixedString.StaticHolder.DEFAULT_PREFIX + "mouseup"),
	WHEEL(INamespacePrefixedString.StaticHolder.DEFAULT_PREFIX + "wheel"),
	@SuppressWarnings("SpellCheckingInspection")
	INPUT_PRE(INamespacePrefixedString.StaticHolder.DEFAULT_PREFIX + "beforeinput"),
	INPUT_POST(INamespacePrefixedString.StaticHolder.DEFAULT_PREFIX + "input"),
	@SuppressWarnings("SpellCheckingInspection")
	KEY_DOWN(INamespacePrefixedString.StaticHolder.DEFAULT_PREFIX + "keydown"),
	@SuppressWarnings("SpellCheckingInspection")
	KEY_UP(INamespacePrefixedString.StaticHolder.DEFAULT_PREFIX + "keyup"),
	@SuppressWarnings("SpellCheckingInspection")
	COMPOSITION_START(INamespacePrefixedString.StaticHolder.DEFAULT_PREFIX + "compositionstart"),
	@SuppressWarnings("SpellCheckingInspection")
	COMPOSITION_UPDATE(INamespacePrefixedString.StaticHolder.DEFAULT_PREFIX + "compositionupdate"),
	@SuppressWarnings("SpellCheckingInspection")
	COMPOSITION_END(INamespacePrefixedString.StaticHolder.DEFAULT_PREFIX + "compositionend"),
	;

	@NonNls
	protected final String eventTypeString;
	protected final INamespacePrefixedString eventType;

	EnumUIEventDOMType(@NonNls CharSequence eventTypeString) {
		String eventTypeString2 = eventTypeString.toString();
		this.eventTypeString = eventTypeString2;
		this.eventType = ImmutableNamespacePrefixedString.of(eventTypeString2);
	}

	@Override
	@NonNls
	public String getEventTypeString() { return eventTypeString; }

	@Override
	public INamespacePrefixedString getEventType() { return eventType; }
}
