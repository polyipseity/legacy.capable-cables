package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.bus;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.reactivex.rxjava3.subjects.Subject;

public enum UIEventBusEntryPoint {
	;

	@Nullable
	private static volatile Subject<?> eventBus;

	@SuppressWarnings("unchecked")
	public static <T> Subject<T> getEventBus() {
		return (Subject<T>) AssertionUtilities.assertNonnull(eventBus); // COMMENT we do not care about the event type
	}

	public static void setEventBus(Subject<?> eventBus) { UIEventBusEntryPoint.eventBus = eventBus; }
}
