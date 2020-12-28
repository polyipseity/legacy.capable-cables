package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import org.jetbrains.annotations.NonNls;

import java.util.function.Function;

public interface ITimestampedInputData {
	long getTimestamp();

	ITimestampedInputData recreate();

	enum StaticHolder {
		;

		@SuppressWarnings("AutoBoxing")
		@NonNls
		private static final ImmutableMap<String, Function<@Nonnull ITimestampedInputData, @Nullable ?>> OBJECT_VARIABLE_MAP =
				ImmutableMap.<String, Function<@Nonnull ITimestampedInputData, @Nullable ?>>builder()
						.put("timestamp", ITimestampedInputData::getTimestamp)
						.build();

		public static ImmutableMap<String, Function<@Nonnull ITimestampedInputData, @Nullable ?>> getObjectVariableMap() {
			return OBJECT_VARIABLE_MAP;
		}
	}
}
