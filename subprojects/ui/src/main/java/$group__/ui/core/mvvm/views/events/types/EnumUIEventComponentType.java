package $group__.ui.core.mvvm.views.events.types;

import $group__.ui.core.mvvm.views.events.IUIEventType;
import $group__.utilities.structures.INamespacePrefixedString;
import $group__.utilities.structures.ImmutableNamespacePrefixedString;
import org.jetbrains.annotations.NonNls;

public enum EnumUIEventComponentType
		implements IUIEventType {
	CHAR_TYPED(INamespacePrefixedString.StaticHolder.DEFAULT_PREFIX + "char_typed"),
	;

	@NonNls
	protected final String eventTypeString;
	protected final INamespacePrefixedString eventType;

	EnumUIEventComponentType(@SuppressWarnings("SameParameterValue") @NonNls String eventTypeString) {
		this.eventTypeString = eventTypeString;
		this.eventType = new ImmutableNamespacePrefixedString(this.eventTypeString);
	}

	@Override
	@NonNls
	public String getEventTypeString() { return eventTypeString; }

	@Override
	public INamespacePrefixedString getEventType() { return eventType; }
}
