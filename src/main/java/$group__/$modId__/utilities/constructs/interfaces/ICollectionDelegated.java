package $group__.$modId__.utilities.constructs.interfaces;

import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;

import javax.annotation.concurrent.Immutable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static $group__.$modId__.utilities.constructs.interfaces.basic.IImmutablizable.tryToImmutableUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

public interface ICollectionDelegated<C extends Collection<E>, E, T extends ICollectionDelegated<C, E, T>> extends Collection<E>, IIterableDelegated<C, E, T> {
	/* SECTION methods */

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


	/* SECTION static classes */

	@Immutable
	interface IImmutable<I extends Collection<E>, E, T extends IImmutable<I, E, T>> extends ICollectionDelegated<I, E, T>, IIterableDelegated.IImmutable<I, E, T> {
		/* SECTION methods */

		@SuppressWarnings("deprecation")
		@Override
		@Deprecated
		default void setIterable(I iterable) { IIterableDelegated.IImmutable.super.setIterable(iterable); }

		@Override
		@Deprecated
		default void setCollection(I collection) { throw rejectUnsupportedOperation(); }

		@Override
		@Deprecated
		default boolean add(E e) { throw rejectUnsupportedOperation(); }

		@Override
		@Deprecated
		default boolean remove(Object o) { throw rejectUnsupportedOperation(); }


		@Override
		@Deprecated
		default boolean addAll(Collection<? extends E> c) { throw rejectUnsupportedOperation(); }

		@Override
		@Deprecated
		default boolean removeAll(Collection<?> c) { throw rejectUnsupportedOperation(); }

		@Override
		@Deprecated
		default boolean removeIf(Predicate<? super E> filter) { throw rejectUnsupportedOperation(); }

		@Override
		@Deprecated
		default boolean retainAll(Collection<?> c) { throw rejectUnsupportedOperation(); }

		@Override
		@Deprecated
		default void clear() { throw rejectUnsupportedOperation(); }


		/* SECTION static classes */

		@Immutable
		class Impl<C extends Collection<E>, E, T extends Impl<C, E, T>> extends ICollectionDelegated.Impl<C, E, T> implements ICollectionDelegated.IImmutable<C, E, T> {
			/* SECTION constructors */

			public Impl(C collection) { super(tryToImmutableUnboxedNonnull(collection)); }

			public Impl(ICollectionDelegated<C, E, ?> copy) { this(copy.getCollection()); }


			/* SECTION getters & setters */

			@SuppressWarnings("deprecation")
			@Override
			@Deprecated
			public void setCollection(C collection) { ICollectionDelegated.IImmutable.super.setIterable(collection); }


			/* SECTION methods */

			@Override
			@OverridingStatus(group = GROUP)
			public final T toImmutable() { return ICollectionDelegated.IImmutable.super.toImmutable(); }

			@Override
			@OverridingStatus(group = GROUP)
			public final boolean isImmutable() { return ICollectionDelegated.IImmutable.super.isImmutable(); }
		}
	}


	class Impl<C extends Collection<E>, E, T extends Impl<C, E, T>> extends IIterableDelegated.Impl<C, E, T> implements ICollectionDelegated<C, E, T> {
		/* SECTION constructors */

		public Impl(C collection) { super(collection); }

		public Impl(ICollectionDelegated<C, E, ?> copy) { this(copy.getCollection()); }


		/* SECTION getters & setters */

		@SuppressWarnings("deprecation")
		@Override
		@Deprecated
		public C getIterable() { return getCollection(); }

		@SuppressWarnings("deprecation")
		@Override
		@Deprecated
		public void setIterable(C iterable) { setCollection(iterable); }

		@Override
		public C getCollection() { return value; }

		@Override
		public void setCollection(C collection) { value = collection; }


		/* SECTION methods */

		@Override
		public T toImmutable() { return castUncheckedUnboxedNonnull(new ICollectionDelegated.IImmutable.Impl<>(this)); }
	}
}
