package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import org.jetbrains.annotations.NonNls;

import java.util.Map;
import java.util.function.Function;

public interface ITuple3<L, M, R>
		extends ITuple {
	L getLeft();

	M getMiddle();

	R getRight();

	enum StaticHolder {
		;

		private static final @Immutable @NonNls Map<String, Function<@Nonnull ITuple3<?, ?, ?>, @Nullable ?>> OBJECT_VARIABLE_MAP =
				ImmutableMap.<String, Function<@Nonnull ITuple3<?, ?, ?>, @Nullable ?>>builder()
						.put("left", ITuple3::getLeft)
						.put("middle", ITuple3::getMiddle)
						.put("right", ITuple3::getRight)
						.build();

		public static @Immutable @NonNls Map<String, Function<@Nonnull ITuple3<?, ?, ?>, @Nullable ?>> getObjectVariableMap() {
			return OBJECT_VARIABLE_MAP;
		}
	}
}
