package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.lang.annotation.Annotation;

@SuppressWarnings("ClassExplicitlyAnnotation")
@Immutable
// COMMENT a very cursed class
public final class ImmutableSubscribeEvent
		implements SubscribeEvent {
	private final EventPriority priority;
	private final boolean receiveCanceled;

	private ImmutableSubscribeEvent(EventPriority priority, boolean receiveCanceled) {
		this.priority = priority;
		this.receiveCanceled = receiveCanceled;
	}

	public static ImmutableSubscribeEvent of(EventPriority priority, boolean receiveCanceled) {
		return new ImmutableSubscribeEvent(priority, receiveCanceled);
	}

	public static ImmutableSubscribeEvent of(boolean receiveCanceled) {
		return of(EventPriority.NORMAL, receiveCanceled);
	}

	public static ImmutableSubscribeEvent of(EventPriority priority) {
		return of(priority, false);
	}

	public static ImmutableSubscribeEvent of() {
		return of(EventPriority.NORMAL, false);
	}

	@Override
	public EventPriority priority() { return priority; }

	@Override
	public boolean receiveCanceled() { return receiveCanceled; }

	@Override
	public Class<? extends Annotation> annotationType() { return SubscribeEvent.class; }
}
