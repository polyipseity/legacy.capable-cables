package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.events.bus;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.EventBusForge;
import io.reactivex.rxjava3.subjects.Subject;

public enum UIEventBusEntryPoint {
	;

	protected static volatile Subject<?> eventBus = EventBusForge.getUIEventBus();

	@SuppressWarnings("unchecked")
	public static <T> Subject<T> getEventBus() {
		return (Subject<T>) eventBus; // COMMENT we do not care about the event type
	}

	public static void setEventBus(Subject<?> eventBus) { UIEventBusEntryPoint.eventBus = eventBus; }
}
