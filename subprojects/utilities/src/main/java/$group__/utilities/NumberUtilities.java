package $group__.utilities;

import java.util.Optional;

import static java.lang.Double.doubleToLongBits;

public enum NumberUtilities {
	;

	@SuppressWarnings("MagicNumber")
	public static boolean isNegative(Number value) {
		double vd = value.doubleValue();
		return (doubleToLongBits(vd) & 0b1000000000000000000000000000000000000000000000000000000000000000L) != 0L && !Double.isNaN(vd);
	}

	public static boolean isDouble(String string) { return tryParseDouble(string).isPresent(); }

	public static Optional<Double> tryParseDouble(String string) {
		return ThrowableUtilities.Try.call(() ->
				Double.parseDouble(string), UtilitiesConfiguration.INSTANCE.getLogger());
	}
}
