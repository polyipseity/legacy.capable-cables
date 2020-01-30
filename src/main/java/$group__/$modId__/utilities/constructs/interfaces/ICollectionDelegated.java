package $group__.$modId__.utilities.constructs.interfaces;

import javax.annotation.concurrent.Immutable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;

public interface ICollectionDelegated<C extends Collection<E>, E, T extends ICollectionDelegated<C, E, T>> extends Collection<E>, IIterableDelegated<C, E, T> {
	/* SECTION methods */

	C getCollection();

	void setCollection(C collection);

	@Override
	@Deprecated
	default C getIterable() { return getCollection(); }

	@Override
	@Deprecated
	default void setIterable(C iterable) { setCollection(iterable); }


	@Override
	default int size() { return getCollection().size(); }

	@Override
	default boolean isEmpty() { return getCollection().isEmpty(); }

	@Override
	default boolean contains(Object o) { return getCollection().contains(o); }

	@SuppressWarnings("EmptyMethod")
	@Override
	default Iterator<E> iterator() { return IIterableDelegated.super.iterator(); }

	@Override
	default Object[] toArray() { return getCollection().toArray(); }

	@SuppressWarnings("SuspiciousToArrayCall")
	@Override
	default <A> A[] toArray(A[] a) { return getCollection().toArray(a); }


	@Override
	default boolean add(E e) { return getCollection().add(e); }

	@Override
	default boolean remove(Object o) { return getCollection().remove(o); }


	@Override
	default boolean containsAll(Collection<?> c) { return getCollection().containsAll(c); }

	@Override
	default boolean addAll(Collection<? extends E> c) { return getCollection().addAll(c); }

	@Override
	default boolean removeAll(Collection<?> c) { return getCollection().removeAll(c); }

	@Override
	default boolean removeIf(Predicate<? super E> filter) { return getCollection().removeIf(filter); }

	@Override
	default boolean retainAll(Collection<?> c) { return getCollection().retainAll(c); }

	@Override
	default void clear() { getCollection().clear(); }


	@Override
	boolean equals(Object o);

	@Override
	int hashCode();

	@Override
	default Spliterator<E> spliterator() { return IIterableDelegated.super.spliterator(); }

	@Override
	default Stream<E> stream() { return getCollection().stream(); }

	@Override
	default Stream<E> parallelStream() { return getCollection().parallelStream(); }


	/* SECTION static classes */

	@Immutable
	interface IImmutable<I extends Collection<E>, E, T extends IImmutable<I, E, T>> extends ICollectionDelegated<I, E, T>, IIterableDelegated.IImmutable<I, E, T> {
		/* SECTION methods */

		@Override
		default void setCollection(I collection) { throw rejectUnsupportedOperation(); }

		@SuppressWarnings("deprecation")
		@Override
		@Deprecated
		default void setIterable(I iterable) { IIterableDelegated.IImmutable.super.setIterable(iterable); }


		@Override
		default boolean add(E e) { throw rejectUnsupportedOperation(); }

		@Override
		default boolean remove(Object o) { throw rejectUnsupportedOperation(); }


		@Override
		default boolean addAll(Collection<? extends E> c) { throw rejectUnsupportedOperation(); }

		@Override
		default boolean removeAll(Collection<?> c) { throw rejectUnsupportedOperation(); }

		@Override
		default boolean removeIf(Predicate<? super E> filter) { throw rejectUnsupportedOperation(); }

		@Override
		default boolean retainAll(Collection<?> c) { throw rejectUnsupportedOperation(); }

		@Override
		default void clear() { throw rejectUnsupportedOperation(); }
	}
}
