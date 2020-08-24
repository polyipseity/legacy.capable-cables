package $group__.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static java.lang.Double.doubleToLongBits;

public enum NumberUtilities {
	;

	private static final Logger LOGGER = LogManager.getLogger();

	@SuppressWarnings("MagicNumber")
	public static boolean isNegative(Number value) {
		double vd = value.doubleValue();
		return (doubleToLongBits(vd) & 0b1000000000000000000000000000000000000000000000000000000000000000L) != 0L && !Double.isNaN(vd);
	}

	public static boolean isDouble(String string) { return tryParseDouble(string).isPresent(); }

	public static Optional<Double> tryParseDouble(String string) {
		return ThrowableUtilities.Try.call(() ->
				Double.parseDouble(string), LOGGER);
	}
}
