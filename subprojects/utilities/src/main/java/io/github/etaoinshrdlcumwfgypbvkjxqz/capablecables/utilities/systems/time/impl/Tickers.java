package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.time.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.time.def.ITicker;

import java.util.function.LongSupplier;

public enum Tickers
		implements ITicker {
	SYSTEM(System::nanoTime),
	;

	private final LongSupplier reader;

	Tickers(LongSupplier reader) {
		this.reader = reader;
	}

	@Override
	public long read() {
		return getReader().getAsLong();
	}

	protected LongSupplier getReader() {
		return reader;
	}
}
