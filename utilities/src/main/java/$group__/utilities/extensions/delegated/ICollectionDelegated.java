package $group__.utilities.extensions.delegated;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface ICollectionDelegated<C extends Collection<E>, E> extends Collection<E>, IIterableDelegated<C, E> {
	@Override
	@Deprecated
	default C getIterable() { return getCollection(); }

	@Override
	@Deprecated
	default void setIterable(C iterable) { setCollection(iterable); }

	C getCollection();

	void setCollection(C collection);

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
}
