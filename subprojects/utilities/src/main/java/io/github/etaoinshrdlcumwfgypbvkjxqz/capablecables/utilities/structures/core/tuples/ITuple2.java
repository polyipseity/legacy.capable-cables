package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import org.jetbrains.annotations.NonNls;

import java.util.Map;
import java.util.function.Function;

public interface ITuple2<L, R>
		extends ITuple {
	L getLeft();

	R getRight();

	ITuple2<R, L> swap();

	@Override
	int hashCode();

	@Override
	boolean equals(Object obj);

	@Override
	String toString();

	enum StaticHolder {
		;

		private static final @Immutable @NonNls Map<String, Function<ITuple2<?, ?>, ?>> OBJECT_VARIABLE_MAP =
				ImmutableMap.<String, Function<ITuple2<?, ?>, ?>>builder()
						.put("left", ITuple2::getLeft)
						.put("right", ITuple2::getRight)
						.build();

		public static @Immutable Map<String, Function<ITuple2<?, ?>, ?>> getObjectVariableMap() {
			return OBJECT_VARIABLE_MAP;
		}
	}
}
