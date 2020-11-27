package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections;

import java.util.List;
import java.util.OptionalInt;

public enum CollectionUtilities {
	;

	public static <T> OptionalInt indexOf(List<T> instance, T o) {
		int result = instance.indexOf(o);
		return result == -1 ? OptionalInt.empty() : OptionalInt.of(result);
	}

	public static <T> OptionalInt lastIndexOf(List<T> instance, T o) {
		int result = instance.lastIndexOf(o);
		return result == -1 ? OptionalInt.empty() : OptionalInt.of(result);
	}
}
