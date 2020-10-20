package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.time.core;

@FunctionalInterface
public interface ITicker {
	long read();

	enum StaticHolder {
		;

		private static final ITicker SYSTEM_TICKER = System::nanoTime;

		public static ITicker getSystemTicker() { return SYSTEM_TICKER; }
	}
}
