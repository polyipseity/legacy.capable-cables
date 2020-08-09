package $group__.client.ui.mvvm.views.domlike.events;

import $group__.utilities.CapacityUtilities;
import $group__.utilities.ObjectUtilities;
import $group__.utilities.specific.MapUtilities;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventException;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import java.util.function.Function;
import java.util.function.Predicate;

public class UIEventTargetDOMLike implements EventTarget {
	@SuppressWarnings("UnstableApiUsage")
	protected final Multimap<String, EventListenerWithParameters> eventTargetListeners = MultimapBuilder
			.hashKeys(CapacityUtilities.INITIAL_CAPACITY_SMALL)
			.hashSetValues(CapacityUtilities.INITIAL_CAPACITY_SMALL)
			.build();

	@Override
	public void addEventListener(String type, EventListener listener, boolean useCapture) {
		getEventTargetListeners().put(type, new EventListenerWithParameters((UIEventListenerDOMLike) listener, useCapture));
	}

	@Override
	public void removeEventListener(String type, EventListener listener, boolean useCapture) {
		if (getEventTargetListeners().remove(type, new EventListenerWithParameters((UIEventListenerDOMLike) listener, useCapture)))
			((UIEventListenerDOMLike) listener).markAsRemoved();
	}

	@Override
	public boolean dispatchEvent(Event evt) throws EventException {
		if (evt.getType() == null || evt.getType().isEmpty())
			throw new EventException(EventException.UNSPECIFIED_EVENT_TYPE_ERR, "Event type unspecified");
		UIEventDOMLike e = (UIEventDOMLike) evt;
		e.setCurrentTarget(this);
		if (e.isPropagationStopped())
			return !e.isDefaultPrevented();
		ImmutableList<EventListenerWithParameters> ls = ImmutableList.copyOf(getEventTargetListeners().get(evt.getType()));

		Predicate<EventListenerWithParameters> shouldHandle = l -> !l.getListener().isRemoved();
		Predicate<EventListenerWithParameters> shouldHandleFinal = shouldHandle;
		switch (evt.getEventPhase()) {
			case Event.CAPTURING_PHASE:
				shouldHandle = l -> l.isUseCapture() && shouldHandleFinal.test(l);
				break;
			case Event.AT_TARGET:
				break;
			case Event.BUBBLING_PHASE:
				shouldHandle = l -> !l.isUseCapture() && shouldHandleFinal.test(l);
				break;
			default:
				throw new InternalError();
		}

		for (EventListenerWithParameters l : ls) {
			if (shouldHandle.test(l))
				l.getListener().handleEvent(evt);
		}

		return !e.isDefaultPrevented();
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Multimap<String, EventListenerWithParameters> getEventTargetListeners() { return eventTargetListeners; }

	public static class EventListenerWithParameters {
		public static final ImmutableList<Function<EventListenerWithParameters, Object>> OBJECT_VARIABLES = ImmutableList.of(
				EventListenerWithParameters::getListener, EventListenerWithParameters::isUseCapture);
		public static final ImmutableMap<String, Function<EventListenerWithParameters, Object>> OBJECT_VARIABLES_MAP = ImmutableMap.copyOf(MapUtilities.stitchIterables(OBJECT_VARIABLES.size(),
				ImmutableList.of("listener", "useCapture"), OBJECT_VARIABLES));
		protected final UIEventListenerDOMLike listener;
		protected final boolean useCapture;

		public EventListenerWithParameters(UIEventListenerDOMLike listener, boolean useCapture) {
			this.listener = listener;
			this.useCapture = useCapture;
		}

		public UIEventListenerDOMLike getListener() { return listener; }

		public boolean isUseCapture() { return useCapture; }

		@Override
		public int hashCode() { return ObjectUtilities.hashCode(this, null, OBJECT_VARIABLES); }

		@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
		@Override
		public boolean equals(Object obj) { return ObjectUtilities.equals(this, obj, false, null, OBJECT_VARIABLES); }

		@Override
		public String toString() { return ObjectUtilities.toString(this, super::toString, OBJECT_VARIABLES_MAP); }
	}
}
