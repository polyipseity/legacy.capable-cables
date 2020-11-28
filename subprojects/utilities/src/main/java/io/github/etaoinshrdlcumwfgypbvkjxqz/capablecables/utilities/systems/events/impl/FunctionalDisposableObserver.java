package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.DefaultDisposableObserver;

import java.util.function.Consumer;

public class FunctionalDisposableObserver<T>
		extends DefaultDisposableObserver<T> {
	private final Consumer<@Nonnull ? super T> action;

	public FunctionalDisposableObserver(Consumer<@Nonnull ? super T> action) {
		this.action = action;
	}

	@Override
	public void onNext(@Nonnull T t) { getAction().accept(t); }

	protected Consumer<@Nonnull ? super T> getAction() { return action; }
}
