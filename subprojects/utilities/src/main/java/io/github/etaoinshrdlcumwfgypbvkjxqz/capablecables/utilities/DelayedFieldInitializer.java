package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;

import java.util.function.Consumer;

public class DelayedFieldInitializer<T> {
	private final Object initializeLock = new Object();
	@Nullable
	private Consumer<@Nonnull ? super T> initializer;
	private volatile boolean initialized = false;

	public DelayedFieldInitializer(Consumer<@Nonnull ? super T> initializer) {
		this.initializer = initializer;
	}

	public T apply(T field) {
		if (!isInitialized()) {
			// COMMENT 'volatile' should be cheaper than 'synchronized'
			synchronized (getInitializeLock()) {
				if (!isInitialized()) {
					Consumer<@Nonnull ? super T> initializer = getInitializer(); // COMMENT cannot get initializer after marking as initialized
					markInitialized(); // COMMENT prevent re-entrance due to the initializer
					initializer.accept(field);
				}
			}
		}
		return field;
	}

	protected boolean isInitialized() {
		return initialized;
	}

	protected Object getInitializeLock() {
		return initializeLock;
	}

	protected Consumer<@Nonnull ? super T> getInitializer() {
		return AssertionUtilities.assertNonnull(initializer);
	}

	protected void markInitialized() {
		initializer = null; // COMMENT release memory by removing references
		initialized = true; // COMMENT volatile, act as a memory barrier
	}
}
