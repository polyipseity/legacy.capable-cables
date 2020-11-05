package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions;

@FunctionalInterface
public interface UncheckedAutoCloseable
		extends AutoCloseable {
	@Override
	void close();

	enum StaticHolder {
		;

		private static final UncheckedAutoCloseable EMPTY = FunctionUtilities.getEmptyRunnable()::run;

		public static UncheckedAutoCloseable getEmpty() {
			return EMPTY;
		}
	}
}
