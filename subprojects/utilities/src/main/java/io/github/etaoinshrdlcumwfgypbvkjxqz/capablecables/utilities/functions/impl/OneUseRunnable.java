package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.OneUseValue;

public class OneUseRunnable
		implements Runnable {
	private final OneUseValue<Runnable> delegate;

	public OneUseRunnable(Runnable delegate) {
		this.delegate = new OneUseValue<>(delegate);
	}

	@Override
	public void run() {
		getDelegate().getValue()
				.ifPresent(Runnable::run);
	}

	protected OneUseValue<? extends Runnable> getDelegate() {
		return delegate;
	}
}
