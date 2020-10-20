package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.bus;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.EventBusSubject;
import io.reactivex.rxjava3.subjects.Subject;

public enum UIEventBusEntryPoint {
	;

	private static volatile Subject<?> eventBus = EventBusSubject.getUIEventBus();

	@SuppressWarnings("unchecked")
	public static <T> Subject<T> getEventBus() {
		return (Subject<T>) eventBus; // COMMENT we do not care about the event type
	}

	public static void setEventBus(Subject<?> eventBus) { UIEventBusEntryPoint.eventBus = eventBus; }
}
