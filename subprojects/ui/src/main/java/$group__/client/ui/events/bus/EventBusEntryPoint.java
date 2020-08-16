package $group__.client.ui.events.bus;

import io.reactivex.rxjava3.subjects.Subject;

import javax.annotation.Nullable;
import java.util.Objects;

public enum EventBusEntryPoint {
	;

	@Nullable
	protected static volatile Subject<?> eventBus;

	@SuppressWarnings("unchecked")
	public static <T> Subject<T> getEventBus() {
		return (Subject<T>) Objects.requireNonNull(eventBus); // COMMENT we do not care about the event type
	}

	public static void setEventBus(Subject<?> eventBus) { EventBusEntryPoint.eventBus = eventBus; }
}
