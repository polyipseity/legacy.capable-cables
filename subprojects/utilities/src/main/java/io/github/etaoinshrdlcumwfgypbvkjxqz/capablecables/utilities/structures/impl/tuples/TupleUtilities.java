package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.tuples.IIntersection;

import java.lang.ref.WeakReference;
import java.util.Optional;

public enum TupleUtilities {
	;

	@SuppressWarnings("ObjectEquality")
	public static <L, R> Optional<IIntersection<L, R>> flattenWeakIntersection
			(IIntersection<? extends WeakReference<? extends L>, ? extends WeakReference<? extends R>> intersection) {
		return intersection
				.map((left, right) -> {
					@Nullable L left1 = left.get();
					@Nullable R right1 = right.get();
					if (left1 == null || right1 == null)
						return Optional.empty();
					assert left1 == right1;
					return Optional.of(ImmutableIntersection.of(left1, right1));
				});
	}
}
