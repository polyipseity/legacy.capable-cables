package $group__.$modId__.utilities.constructs.interfaces;

import $group__.$modId__.utilities.constructs.interfaces.basic.IAdapter;

import javax.annotation.concurrent.Immutable;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;

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
	}
}
