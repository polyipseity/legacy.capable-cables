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

	enum StaticHolder {
		;

		private static final @Immutable @NonNls Map<String, Function<ITuple2<?, ?>, ?>> OBJECT_VARIABLES_MAP =
				ImmutableMap.<String, Function<ITuple2<?, ?>, ?>>builder()
						.put("left", ITuple2::getLeft)
						.put("right", ITuple2::getRight)
						.build();

		public static @Immutable Map<String, Function<ITuple2<?, ?>, ?>> getObjectVariablesMap() {
			return OBJECT_VARIABLES_MAP;
		}
	}
}
