package $group__.utilities.extensions.delegated;

import $group__.traits.IAdapter;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public interface IIterableDelegated<I extends Iterable<E>, E> extends Iterable<E>, IAdapter<I> {
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
}
