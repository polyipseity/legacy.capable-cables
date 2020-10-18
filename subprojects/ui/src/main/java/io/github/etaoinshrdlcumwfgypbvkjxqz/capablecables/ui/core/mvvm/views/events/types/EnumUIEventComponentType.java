package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.types;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import org.jetbrains.annotations.NonNls;

public enum EnumUIEventComponentType
		implements IUIEventType {
	CHAR_TYPED(StaticHolder.getDefaultPrefix() + "char_typed"),
	;

	@NonNls
	private final String eventTypeString;
	private final INamespacePrefixedString eventType;

	EnumUIEventComponentType(@SuppressWarnings("SameParameterValue") @NonNls CharSequence eventTypeString) {
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
