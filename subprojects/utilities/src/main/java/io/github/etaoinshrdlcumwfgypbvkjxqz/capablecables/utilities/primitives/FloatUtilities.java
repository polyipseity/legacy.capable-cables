package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.primitives;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ComparableUtilities;

public enum FloatUtilities {
	;

	public static float saturatedCast(double d) {
		if (Double.isFinite(d)) {
			if (ComparableUtilities.greaterThan(d, Float.MAX_VALUE))
				return Float.MAX_VALUE;
			if (ComparableUtilities.lessThan(d, Float.MIN_VALUE))
				return Float.MIN_VALUE;
		}
		return (float) d;
	}
}
