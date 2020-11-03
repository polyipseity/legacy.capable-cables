package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public interface IUnion<L, R>
		extends ITuple2<Optional<? extends L>, Optional<? extends R>> {
	static void assertEither(@Nullable Object left, @Nullable Object right) {
		assert left == null || right == null; // COMMENT at least one null
		assert left != null || right != null; // COMMENT at least one nonnull
	}

	<T> T map(Function<? super L, ? extends T> leftMapper, Function<? super R, ? extends T> rightMapper)
			throws IllegalArgumentException;

	<L2, R2> IUnion<L2, R2> mapBoth(Function<? super L, ? extends L2> leftMapper, Function<? super R, ? extends R2> rightMapper)
			throws IllegalArgumentException;

	static <L, R, L2> IUnion<L2, R> mapLeft(IUnion<L, R> instance, Function<? super L, ? extends L2> leftMapper) {
		return instance.mapBoth(leftMapper, Function.identity());
	}

	@Override
	IUnion<R, L> swap();

	static <L, R, R2> IUnion<L, R2> mapRight(IUnion<L, R> instance, Function<? super R, ? extends R2> rightMapper) {
		return instance.mapBoth(Function.identity(), rightMapper);
	}

	static <L extends T, R extends T, T> T get(IUnion<L, R> instance) {
		return instance.map(Function.identity(), Function.identity());
	}

	void accept(Consumer<? super L> leftConsumer, Consumer<R> rightConsumer);
}
