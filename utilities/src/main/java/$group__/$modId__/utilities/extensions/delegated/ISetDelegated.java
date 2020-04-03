package $group__.$modId__.utilities.extensions.delegated;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;

public interface ISetDelegated<S extends Set<E>, E> extends Set<E>, ICollectionDelegated<S, E> {
	/* SECTION methods */

	@Override
	@Deprecated
	default S getCollection() { return getSet(); }

	@Override
	@Deprecated
	default void setCollection(S collection) { setSet(collection); }

	S getSet();

	void setSet(S set);

	@SuppressWarnings("EmptyMethod")
	@Override
	default int size() { return ICollectionDelegated.super.size(); }

	@SuppressWarnings("EmptyMethod")
	@Override
	default boolean isEmpty() { return ICollectionDelegated.super.isEmpty(); }

	@Override
	default boolean contains(Object o) { return ICollectionDelegated.super.contains(o); }

	@Override
	default Iterator<E> iterator() { return ICollectionDelegated.super.iterator(); }

	@SuppressWarnings("EmptyMethod")
	@Override
	default Object[] toArray() { return ICollectionDelegated.super.toArray(); }

	@SuppressWarnings("SuspiciousToArrayCall")
	@Override
	default <A> A[] toArray(A[] a) { return ICollectionDelegated.super.toArray(a); }


	@Override
	default boolean add(E e) { return ICollectionDelegated.super.add(e); }

	@Override
	default boolean remove(Object o) { return ICollectionDelegated.super.remove(o); }


	@Override
	default boolean containsAll(Collection<?> c) { return ICollectionDelegated.super.containsAll(c); }

	@Override
	default boolean addAll(Collection<? extends E> c) { return ICollectionDelegated.super.addAll(c); }

	@Override
	default boolean retainAll(Collection<?> c) { return ICollectionDelegated.super.retainAll(c); }

	@Override
	default boolean removeAll(Collection<?> c) { return ICollectionDelegated.super.removeAll(c); }

	@SuppressWarnings("EmptyMethod")
	@Override
	default void clear() { ICollectionDelegated.super.clear(); }


	@Override
	boolean equals(Object o);

	@Override
	int hashCode();

	@Override
	default Spliterator<E> spliterator() { return ICollectionDelegated.super.spliterator(); }
}
