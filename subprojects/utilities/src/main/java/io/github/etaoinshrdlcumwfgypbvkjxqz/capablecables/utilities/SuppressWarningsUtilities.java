package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import java.util.function.Supplier;

public enum SuppressWarningsUtilities {
	;

	// CODE suppressThisEscapedWarning(() -> this)
	public static <T> T suppressThisEscapedWarning(Supplier<? extends T> thisSupplier) {
		return thisSupplier.get();
	}
}
