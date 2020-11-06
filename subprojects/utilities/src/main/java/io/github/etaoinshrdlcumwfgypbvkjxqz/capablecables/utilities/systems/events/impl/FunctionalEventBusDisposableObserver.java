package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.DefaultDisposableObserver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.core.ISubscribeEventProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;
import java.util.function.Consumer;

public class FunctionalEventBusDisposableObserver<T>
		extends DefaultDisposableObserver<T>
		implements ISubscribeEventProvider {
	@Nullable
	private final SubscribeEvent subscribeEvent;
	private final Consumer<@Nonnull ? super T> action;

	public FunctionalEventBusDisposableObserver(Consumer<@Nonnull ? super T> action) { this(null, action); }

	public FunctionalEventBusDisposableObserver(@Nullable SubscribeEvent subscribeEvent, Consumer<@Nonnull ? super T> action) {
		this.subscribeEvent = subscribeEvent;
		this.action = action;
	}

	@Override
	public void onNext(@Nonnull T t) { getAction().accept(t); }

	protected Consumer<@Nonnull ? super T> getAction() { return action; }

	@Override
	public Optional<? extends SubscribeEvent> getSubscribeEvent() { return Optional.ofNullable(subscribeEvent); }
}
