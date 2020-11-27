package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;

import java.util.function.BiFunction;

public interface IIntersection<L, R>
		extends ITuple2<L, R> {
	<T> T map(BiFunction<@Nonnull ? super L, @Nonnull ? super R, @Nonnull ? extends T> mapper);

	@Override
	IIntersection<R, L> swap();
}
