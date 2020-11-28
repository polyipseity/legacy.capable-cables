package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.DelegatingDisposableObserver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.core.ISubscribeEventProvider;
import io.reactivex.rxjava3.observers.DisposableObserver;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;

public class SubscribeEventDisposableObserver<T>
		extends DelegatingDisposableObserver<T>
		implements ISubscribeEventProvider {
	@Nullable
	private final SubscribeEvent subscribeEvent;

	public SubscribeEventDisposableObserver(@Nullable SubscribeEvent subscribeEvent, DisposableObserver<? super T> delegate) {
		super(delegate);
		this.subscribeEvent = subscribeEvent;
	}

	@Override
	public Optional<? extends SubscribeEvent> getSubscribeEvent() {
		return Optional.ofNullable(subscribeEvent);
	}
}
