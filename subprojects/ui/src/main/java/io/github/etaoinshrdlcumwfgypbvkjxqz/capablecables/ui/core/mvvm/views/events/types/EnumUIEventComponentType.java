package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.types;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableIdentifier;
import org.jetbrains.annotations.NonNls;

public enum EnumUIEventComponentType
		implements IUIEventType {
	CHAR_TYPED(StaticHolder.getDefaultPrefix() + "char_typed"),
	;

	@NonNls
	private final String eventTypeString;
	private final IIdentifier eventType;

	EnumUIEventComponentType(@SuppressWarnings("SameParameterValue") @NonNls CharSequence eventTypeString) {
		this.eventType = ImmutableIdentifier.ofInterning(this.eventTypeString = eventTypeString.toString());
	}

	@Override
	@NonNls
	public String getEventTypeString() { return eventTypeString; }

	@Override
	public IIdentifier getEventType() { return eventType; }
}
