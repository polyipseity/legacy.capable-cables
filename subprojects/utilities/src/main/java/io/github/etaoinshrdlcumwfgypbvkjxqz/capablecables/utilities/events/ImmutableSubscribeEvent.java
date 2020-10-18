package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.lang.annotation.Annotation;

@SuppressWarnings("ClassExplicitlyAnnotation")
@Immutable
public final class ImmutableSubscribeEvent
		implements SubscribeEvent {
	private final EventPriority priority;
	private final boolean receiveCanceled;

	public ImmutableSubscribeEvent(EventPriority priority, boolean receiveCanceled) {
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
