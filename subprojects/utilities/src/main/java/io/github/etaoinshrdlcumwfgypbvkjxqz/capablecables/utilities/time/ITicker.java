package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.time;

@FunctionalInterface
public interface ITicker {
	long read();

	enum StaticHolder {
		;

		private static final ITicker SYSTEM_TICKER = System::nanoTime;

		public static ITicker getSystemTicker() { return SYSTEM_TICKER; }
	}
}
