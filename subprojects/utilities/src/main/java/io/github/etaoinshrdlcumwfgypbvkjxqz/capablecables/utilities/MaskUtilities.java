package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

public enum MaskUtilities {
	;

	public static boolean containsAll(long mask, long bits) {
		return (mask & bits) == bits;
	}
}
