package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.primitives;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntIterable;

public enum PrimitiveCollectionUtilities {
	;

	public static int[] toArray(IntIterable iterable) {
		IntCollection collection;
		if (iterable instanceof IntCollection)
			collection = (IntCollection) iterable;
		else
			collection = new IntArrayList(iterable.iterator());
		return collection.toIntArray();
	}
}
