package $group__.$modId__.utilities.constructs.interfaces;

import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.constructs.interfaces.basic.IAdapter;

import javax.annotation.concurrent.Immutable;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

import static $group__.$modId__.utilities.constructs.interfaces.basic.IImmutablizable.tryToImmutableUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

public interface IIterableDelegated<I extends Iterable<E>, E, T extends IIterableDelegated<I, E, T>> extends Iterable<E>, IAdapter<I, T> {
	/* SECTION methods */

	@Override
	@Deprecated
	default I get() { return getIterable(); }

	@Override
	@Deprecated
	default void set(I value) { setIterable(value); }

	I getIterable();

	void setIterable(I iterable);

	@Override
	default Iterator<E> iterator() { return getIterable().iterator(); }

	@Override
	default void forEach(Consumer<? super E> action) { getIterable().forEach(action); }

	@Override
	default Spliterator<E> spliterator() { return getIterable().spliterator(); }


	/* SECTION static classes */

	@Immutable
	interface IImmutable<I extends Iterable<E>, E, T extends IImmutable<I, E, T>> extends IIterableDelegated<I, E, T>, IAdapter.IImmutable<I, T> {
		/* SECTION methods */

		@SuppressWarnings("deprecation")
		@Override
		@Deprecated
		default void set(I value) { IAdapter.IImmutable.super.set(value); }

		@Override
		@Deprecated
		default void setIterable(I iterable) { throw rejectUnsupportedOperation(); }


		/* SECTION static classes */

		@Immutable
		class Impl<I extends Iterable<E>, E, T extends Impl<I, E, T>> extends IIterableDelegated.Impl<I, E, T> implements IIterableDelegated.IImmutable<I, E, T> {
			/* SECTION constructors */

			public Impl(I iterable) { super(tryToImmutableUnboxedNonnull(iterable)); }

			public Impl(IIterableDelegated<I, E, ?> copy) { this(copy.getIterable()); }


			/* SECTION getters & setters */

			@SuppressWarnings("deprecation")
			@Override
			@Deprecated
			public void setIterable(I iterable) { IIterableDelegated.IImmutable.super.setIterable(iterable); }


			/* SECTION methods */

			@Override
			@OverridingStatus(group = GROUP)
			public final T toImmutable() { return IIterableDelegated.IImmutable.super.toImmutable(); }

			@Override
			@OverridingStatus(group = GROUP)
			public final boolean isImmutable() { return IIterableDelegated.IImmutable.super.isImmutable(); }
		}
	}


	class Impl<I extends Iterable<E>, E, T extends Impl<I, E, T>> extends IAdapter.Impl<I, T> implements IIterableDelegated<I, E, T> {
		/* SECTION constructors */

		public Impl(I iterable) { super(iterable); }

		public Impl(IIterableDelegated<I, E, ?> copy) { this(copy.getIterable()); }


		/* SECTION getters & setters */

		@SuppressWarnings("deprecation")
		@Override
		@Deprecated
		public void set(I value) { setIterable(value); }

		@SuppressWarnings("deprecation")
		@Override
		@Deprecated
		public I get() { return getIterable(); }

		@Override
		public I getIterable() { return value; }

		@Override
		public void setIterable(I iterable) { value = iterable; }


		/* SECTION methods */

		@Override
		public T toImmutable() { return castUncheckedUnboxedNonnull(new IIterableDelegated.IImmutable.Impl<>(this)); }

		@Override
		public boolean isImmutable() { return false; }
	}
}
