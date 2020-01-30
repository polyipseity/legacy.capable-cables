package $group__.$modId__.utilities.constructs.interfaces;

import javax.annotation.concurrent.Immutable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Predicate;

import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;

public interface ISetDelegated<S extends Set<E>, E, T extends ISetDelegated<S, E, T>> extends Set<E>, ICollectionDelegated<S, E, T> {
	/* SECTION methods */

	S getSet();

	void setSet(S set);

	@Override
	@Deprecated
	default S getCollection() { return getSet(); }

	@Override
	@Deprecated
	default void setCollection(S collection) { setSet(collection); }


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


	/* SECTION static classes */

	@Immutable
	interface IImmutable<S extends Set<E>, E, T extends IImmutable<S, E, T>> extends ISetDelegated<S, E, T>, ICollectionDelegated.IImmutable<S, E, T> {
		/* SECTION methods */

		@Override
		default void setSet(S set) { throw rejectUnsupportedOperation(); }

		@SuppressWarnings("deprecation")
		@Override
		@Deprecated
		default void setCollection(S collection) { ICollectionDelegated.IImmutable.super.setCollection(collection); }


		@Override
		default boolean add(E e) { return ICollectionDelegated.IImmutable.super.add(e); }

		@Override
		default boolean remove(Object o) { return ICollectionDelegated.IImmutable.super.remove(o); }


		@Override
		default boolean addAll(Collection<? extends E> c) { return ICollectionDelegated.IImmutable.super.addAll(c); }

		@Override
		default boolean removeAll(Collection<?> c) { return ICollectionDelegated.IImmutable.super.removeAll(c); }

		@Override
		default boolean removeIf(Predicate<? super E> filter) { return ICollectionDelegated.IImmutable.super.removeIf(filter); }

		@Override
		default boolean retainAll(Collection<?> c) { return ICollectionDelegated.IImmutable.super.retainAll(c); }

		@Override
		default void clear() { ICollectionDelegated.IImmutable.super.clear(); }
	}
}
