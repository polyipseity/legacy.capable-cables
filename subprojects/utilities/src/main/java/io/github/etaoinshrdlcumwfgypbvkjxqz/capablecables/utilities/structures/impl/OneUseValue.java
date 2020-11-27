package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IValue;

import java.util.Optional;

public class OneUseValue<T>
		implements IValue<T> {
	private final Object useLock = new Object();
	private @Nullable T value;
	private volatile boolean available = true;

	public OneUseValue(T value) {
		this.value = value;
	}

	@Override
	public Optional<? extends T> getValue() {
		if (isAvailable()) { // COMMENT avoid negation
			// COMMENT 'volatile' should be cheaper than 'synchronized'
			synchronized (getUseLock()) {
				if (isAvailable()) {
					T result = value; // COMMENT cannot get delegate after marking as invoked
					markInitialized(); // COMMENT prevent re-entrance due to the delegate
					return Optional.ofNullable(result);
				}
			}
		}
		return Optional.empty();
	}

	protected boolean isAvailable() {
		return available;
	}

	protected Object getUseLock() {
		return useLock;
	}

	protected void markInitialized() {
		value = null; // COMMENT release memory by removing references
		available = false; // COMMENT volatile, act as a memory barrier
	}
}
