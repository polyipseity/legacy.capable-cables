package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.OneUseValue;

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
