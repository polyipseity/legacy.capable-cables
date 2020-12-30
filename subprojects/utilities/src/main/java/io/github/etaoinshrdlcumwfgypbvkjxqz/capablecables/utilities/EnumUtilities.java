package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public enum EnumUtilities {
	;

	@SuppressWarnings({"UnstableApiUsage", "rawtypes", "RedundantSuppression"})
	public static boolean areNamesCompatible(Iterator<? extends Class<? extends Enum<?>>> enums) {
		long count = Streams.stream(enums).unordered()
				.map(Class::getEnumConstants)
				.peek(Objects::requireNonNull)
				.map(Arrays::stream)
				.map(enumConstantsStream -> enumConstantsStream.map(Enum::name))
				.map(enumConstantsStream -> enumConstantsStream.collect(ImmutableSet.toImmutableSet()))
				.distinct()
				.count();
		/* COMMENT
		1 means every name set is the same.
		0 means input is empty.
		 */
		return count == 1L || count == 0L;
	}
}
