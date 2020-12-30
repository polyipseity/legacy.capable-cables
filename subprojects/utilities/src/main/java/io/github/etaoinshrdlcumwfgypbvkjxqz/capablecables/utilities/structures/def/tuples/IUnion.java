package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.tuples;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public interface IUnion<L, R>
		extends ITuple2<Optional<? extends L>, Optional<? extends R>> {
	static void assertEither(@Nullable Object left, @Nullable Object right) {
		assert left == null || right == null; // COMMENT at least one null
		assert left != null || right != null; // COMMENT at least one nonnull
	}

	static <L, R, L2> IUnion<L2, R> mapLeft(IUnion<L, R> instance, Function<@Nonnull ? super L, @Nonnull ? extends L2> leftMapper) {
		return instance.mapBoth(leftMapper, Function.identity());
	}

	<L2, R2> IUnion<L2, R2> mapBoth(Function<@Nonnull ? super L, @Nonnull ? extends L2> leftMapper, Function<@Nonnull ? super R, @Nonnull ? extends R2> rightMapper)
			throws IllegalArgumentException;

	static <L, R, R2> IUnion<L, R2> mapRight(IUnion<L, R> instance, Function<@Nonnull ? super R, @Nonnull ? extends R2> rightMapper) {
		return instance.mapBoth(Function.identity(), rightMapper);
	}

	static <L extends T, R extends T, T> T get(IUnion<L, R> instance) {
		return instance.map(Function.identity(), Function.identity());
	}

	<T> T map(Function<@Nonnull ? super L, @Nonnull ? extends T> leftMapper, Function<@Nonnull ? super R, @Nonnull ? extends T> rightMapper)
			throws IllegalArgumentException;

	@Override
	IUnion<R, L> swap();

	IUnion<L, R> accept(Consumer<@Nonnull ? super L> leftConsumer, Consumer<@Nonnull ? super R> rightConsumer);
}
