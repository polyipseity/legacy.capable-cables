package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.primitives;

import it.unimi.dsi.fastutil.ints.IntIterable;

import java.util.Arrays;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public enum PrimitiveStreamUtilities {
	;

	public static IntStream stream(PrimitiveIterator.OfInt iterator) {
		return stream(Spliterators.spliteratorUnknownSize(iterator, 0));
	}

	public static IntStream stream(Spliterator.OfInt spliterator) {
		return StreamSupport.intStream(spliterator, false);
	}

	public static IntStream stream(IntIterable iterable) {
		return stream(Arrays.spliterator(PrimitiveCollectionUtilities.toArray(iterable)));
	}
}
