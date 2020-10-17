package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import java.util.Optional;

import static java.lang.Double.doubleToLongBits;

public enum NumberUtilities {
	;

	@SuppressWarnings("MagicNumber")
	public static boolean isNegative(Number value) {
		double vd = value.doubleValue();
		return (doubleToLongBits(vd) & 0b1000000000000000000000000000000000000000000000000000000000000000L) != 0L && !Double.isNaN(vd);
	}

	public static boolean isDouble(CharSequence string) { return tryParseDouble(string).isPresent(); }

	public static Optional<Double> tryParseDouble(CharSequence string) {
		try {
			return Optional.of(Double.parseDouble(string.toString()));
		} catch (NumberFormatException ex) {
			return Optional.empty();
		}
	}
}
