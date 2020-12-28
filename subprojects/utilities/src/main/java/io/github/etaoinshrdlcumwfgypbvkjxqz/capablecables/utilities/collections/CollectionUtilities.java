package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;

import java.lang.ref.Reference;
import java.util.*;

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

	public static <T> long countEqualPrefixes(Iterable<? extends T> a, Iterable<? extends T> b) {
		long result = 0;
		Iterator<? extends T>
				aIterator = a.iterator(),
				bIterator = b.iterator();
		while (aIterator.hasNext() && bIterator.hasNext()) {
			if (Objects.equals(aIterator.next(), bIterator.next()))
				++result;
			else
				break;
		}
		return result;
	}

	public static <T> @Immutable List<T> collectOrRemoveReferences(Iterable<? extends Reference<? extends T>> instance) {
		List<T> result = new ArrayList<>(
				Math.toIntExact(
						SpliteratorUtilities.estimateSizeOrElse(instance.spliterator(),
								CapacityUtilities.getInitialCapacityMedium())
				)
		);
		for (Iterator<? extends Reference<? extends T>> iterator = instance.iterator();
		     iterator.hasNext(); ) {
			Reference<? extends T> reference = iterator.next();
			@Nullable T value = reference.get();
			if (value == null)
				iterator.remove();
			else
				result.add(value);
		}
		return ImmutableList.copyOf(result);
	}
}
