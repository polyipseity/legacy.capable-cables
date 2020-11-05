package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core;

import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NonNls;

import java.util.function.Function;

public interface ITimestampedInputData {
	long getTimestamp();

	ITimestampedInputData recreate();

	enum StaticHolder {
		;

		@NonNls
		private static final ImmutableMap<String, Function<ITimestampedInputData, ?>> OBJECT_VARIABLE_MAP =
				ImmutableMap.<String, Function<ITimestampedInputData, ?>>builder()
						.put("timestamp", ITimestampedInputData::getTimestamp)
						.build();

		public static ImmutableMap<String, Function<ITimestampedInputData, ?>> getObjectVariableMap() {
			return OBJECT_VARIABLE_MAP;
		}
	}
}
