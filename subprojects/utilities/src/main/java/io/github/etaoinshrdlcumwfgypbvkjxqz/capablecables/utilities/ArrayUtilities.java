package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.ICloneable;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public enum ArrayUtilities {
	;

	private static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
	private static final long[] EMPTY_LONG_ARRAY = new long[0];

	public static double[] getEmptyDoubleArray() {
		return EMPTY_DOUBLE_ARRAY;
	}

	public static long[] getEmptyLongArray() {
		return EMPTY_LONG_ARRAY;
	}

	public static boolean isAllElementsNonnull(Object... array) {
		return Arrays.stream(array)
				.allMatch(Objects::nonNull);
	}

	public static boolean isNested(Object array)
			throws IllegalArgumentException {
		return getComponentType(array).isArray();
	}

	public static Class<?> getComponentType(Object array)
			throws IllegalArgumentException {
		return Optional.ofNullable(array.getClass().getComponentType() /* COMMENT always safe by contract */)
				.orElseThrow(IllegalArgumentException::new);
	}

	public static boolean isEmpty(Object array)
			throws IllegalArgumentException {
		return Array.getLength(array) == 0;
	}

	public static <T> boolean contains(T[] array, Object element) {
		return Arrays.stream(array).unordered()
				.anyMatch(Predicate.isEqual(element));
	}

	@SuppressWarnings({"unchecked"})
	public static <T, TNestedElement> T deepClone(T array)
			throws IllegalArgumentException {
		Class<?> componentType = getComponentType(array); // COMMENT checks whether 'array' is actually an array as a side effect
		// COMMENT by casting to TNestedElement[][], no cast checks are present
		if (componentType.isArray()) // COMMENT nested
			return (T) Arrays.stream(CastUtilities.<TNestedElement[][]>castUnchecked(array))
					.map(ArrayUtilities::deepClone)
					.toArray(length -> (TNestedElement[][]) Array.newInstance(componentType, length) /* COMMENT should be safe */);
		else // COMMENT not nested
			return (T) ICloneable.clone((Cloneable) array); // COMMENT should be safe
	}
}
