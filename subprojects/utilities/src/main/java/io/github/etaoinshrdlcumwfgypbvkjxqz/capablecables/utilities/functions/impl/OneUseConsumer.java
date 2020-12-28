package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.OneUseValue;

import java.util.function.Consumer;

public class OneUseConsumer<T>
		implements Consumer<T> {
	private final OneUseValue<Consumer<@Nonnull ? super T>> delegate;

	public OneUseConsumer(Consumer<@Nonnull ? super T> delegate) {
		this.delegate = new OneUseValue<>(delegate);
	}

	@Override
	public void accept(T t) {
		getDelegate().getValue()
				.ifPresent(delegate -> delegate.accept(t));
	}

	protected OneUseValue<? extends Consumer<@Nonnull ? super T>> getDelegate() {
		return delegate;
	}
}
