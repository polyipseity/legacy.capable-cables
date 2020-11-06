package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
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

		private static final @Immutable @NonNls Map<String, Function<@Nonnull ITuple2<?, ?>, @Nullable ?>> OBJECT_VARIABLE_MAP =
				ImmutableMap.<String, Function<@Nonnull ITuple2<?, ?>, @Nullable ?>>builder()
						.put("left", ITuple2::getLeft)
						.put("right", ITuple2::getRight)
						.build();

		public static @Immutable Map<String, Function<@Nonnull ITuple2<?, ?>, @Nullable ?>> getObjectVariableMap() {
			return OBJECT_VARIABLE_MAP;
		}
	}
}
