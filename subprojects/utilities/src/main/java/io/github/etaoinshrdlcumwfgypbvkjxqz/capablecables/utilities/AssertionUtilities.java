package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;

public enum AssertionUtilities {
	;

	public static <T> T assertNonnull(@Nullable T o) {
		assert o != null;
		return o;
	}

	public static void assertTrue(boolean o) {
		assert o;
	}
}
