package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events;

import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.lang.annotation.Annotation;

@SuppressWarnings("ClassExplicitlyAnnotation")
public class SubscribeEventObject
		implements SubscribeEvent {
	private final EventPriority priority;
	private final boolean receiveCanceled;

	public SubscribeEventObject(EventPriority priority, boolean receiveCanceled) {
		this.priority = priority;
		this.receiveCanceled = receiveCanceled;
	}

	@Override
	public EventPriority priority() { return priority; }

	@Override
	public boolean receiveCanceled() { return receiveCanceled; }

	@Override
	public Class<? extends Annotation> annotationType() { return SubscribeEvent.class; }
}
