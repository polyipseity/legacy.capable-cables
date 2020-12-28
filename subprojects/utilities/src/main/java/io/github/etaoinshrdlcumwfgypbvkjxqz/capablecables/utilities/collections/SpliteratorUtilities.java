package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections;

import java.util.Spliterator;

public enum SpliteratorUtilities {
	;

	public static long estimateSizeOrElse(Spliterator<?> instance, long orElse) {
		long result = instance.estimateSize();
		if (result == Long.MAX_VALUE)
			return orElse;
		return result;
	}
}
