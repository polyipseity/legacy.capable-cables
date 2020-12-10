package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.ICursor;

public final class ImmutableCursor
		implements ICursor {
	private final long handle;

	private ImmutableCursor(long handle) {
		this.handle = handle;
	}

	public static ImmutableCursor of(long handle) {
		return new ImmutableCursor(handle);
	}

	@Override
	public long getHandle() {
		return handle;
	}

	@Override
	public void close() {
		// COMMENT NOOP
	}
}
