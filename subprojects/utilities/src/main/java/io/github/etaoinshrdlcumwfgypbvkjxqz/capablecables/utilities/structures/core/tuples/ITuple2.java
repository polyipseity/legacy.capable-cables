package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapUtilities;
import org.jetbrains.annotations.NonNls;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface ITuple2<L, R>
		extends ITuple {
	L getLeft();

	R getRight();

	enum StaticHolder {
		;

		private static final @Immutable List<Function<? super ITuple2<?, ?>, ?>> OBJECT_VARIABLES = ImmutableList.of(
				ITuple2::getLeft, ITuple2::getRight);
		private static final @Immutable @NonNls Map<String, Function<? super ITuple2<?, ?>, ?>> OBJECT_VARIABLES_MAP = ImmutableMap.copyOf(MapUtilities.zipKeysValues(
				ImmutableList.of("left", "right"),
				getObjectVariables()));

		public static @Immutable List<Function<? super ITuple2<?, ?>, ?>> getObjectVariables() {
			return OBJECT_VARIABLES;
		}

		public static @Immutable Map<String, Function<? super ITuple2<?, ?>, ?>> getObjectVariablesMap() {
			return OBJECT_VARIABLES_MAP;
		}
	}
}
