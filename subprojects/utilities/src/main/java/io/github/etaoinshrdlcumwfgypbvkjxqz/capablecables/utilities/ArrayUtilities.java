package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import java.util.Arrays;
import java.util.Objects;

public enum ArrayUtilities {
	;

	private static final double[] EMPTY_DOUBLE_ARRAY = new double[0];

	public static double[] getEmptyDoubleArray() {
		return EMPTY_DOUBLE_ARRAY;
	}

	public static boolean isAllElementsNonnull(Object... array) {
		return Arrays.stream(array)
				.allMatch(Objects::nonNull);
	}
}
