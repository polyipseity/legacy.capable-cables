package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.primitives;

import it.unimi.dsi.fastutil.ints.IntIterable;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public enum PrimitiveStreamUtilities {
	;

	public static IntStream streamInt(IntIterable iterable) {
		return StreamSupport.intStream(
				Arrays.spliterator(PrimitiveCollectionUtilities.toArray(iterable)),
				false);
	}
}
