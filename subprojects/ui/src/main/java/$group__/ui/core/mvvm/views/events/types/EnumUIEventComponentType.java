package $group__.ui.core.mvvm.views.events.types;

import $group__.ui.core.mvvm.views.events.IUIEventType;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.NamespacePrefixedString;

public enum EnumUIEventComponentType
		implements IUIEventType {
	CHAR_TYPED(INamespacePrefixedString.DEFAULT_PREFIX + "char_typed"),
	;

	protected final String eventTypeString;
	protected final INamespacePrefixedString eventType;

	EnumUIEventComponentType(@SuppressWarnings("SameParameterValue") String eventTypeString) {
		this.eventTypeString = eventTypeString;
		this.eventType = new NamespacePrefixedString(this.eventTypeString);
	}

	@Override
	public String getEventTypeString() { return eventTypeString; }

	@Override
	public INamespacePrefixedString getEventType() { return eventType; }
}
