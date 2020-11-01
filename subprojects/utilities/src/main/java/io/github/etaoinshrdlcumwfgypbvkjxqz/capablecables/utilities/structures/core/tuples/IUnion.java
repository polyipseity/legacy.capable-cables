package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples;

import javax.annotation.Nullable;
import java.util.Optional;
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

	@Override
	IUnion<R, L> swap();
}
