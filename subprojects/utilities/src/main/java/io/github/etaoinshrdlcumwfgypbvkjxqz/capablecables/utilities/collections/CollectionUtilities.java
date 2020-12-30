package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;

import java.lang.ref.Reference;
import java.util.*;

public enum CollectionUtilities {
	;

	private static final Iterator<?> EMPTY_ITERATOR = EmptyIterator.INSTANCE;

	public static <T> OptionalInt indexOf(List<T> instance, T o) {
		int result = instance.indexOf(o);
		return result == -1 ? OptionalInt.empty() : OptionalInt.of(result);
	}

	public static <T> OptionalInt lastIndexOf(List<T> instance, T o) {
		int result = instance.lastIndexOf(o);
		return result == -1 ? OptionalInt.empty() : OptionalInt.of(result);
	}

	public static <T> long countEqualPrefixes(Iterator<? extends T> a, Iterator<? extends T> b) {
		long result = 0;
		while (a.hasNext() && b.hasNext()) {
			if (Objects.equals(a.next(), b.next()))
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

	public static <E> Iterator<E> iterate(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<? extends E> optional) {
		return optional
				.<Iterator<E>>map(Iterators::singletonIterator)
				.orElseGet(CollectionUtilities::getEmptyIterator);
	}

	@SuppressWarnings("unchecked")
	public static <E> Iterator<E> getEmptyIterator() {
		return (Iterator<E>) EMPTY_ITERATOR; // COMMENT safe, never produces any 'E's
	}

	private enum EmptyIterator
			implements Iterator<Object> {
		INSTANCE,
		;

		@Override
		public boolean hasNext() {
			return false;
		}

		@Override
		public Object next() {
			throw new NoSuchElementException();
		}
	}
}
