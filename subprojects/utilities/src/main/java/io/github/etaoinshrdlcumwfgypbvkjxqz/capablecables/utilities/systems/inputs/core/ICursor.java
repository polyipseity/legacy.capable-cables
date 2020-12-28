package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import org.jetbrains.annotations.NonNls;

import java.util.Map;
import java.util.function.Function;

public interface ICursor
		extends AutoCloseable {
	long getHandle();

	@Override
	void close();

	@Override
	boolean equals(Object obj);

	enum StaticHolder {
		;

		@SuppressWarnings("AutoBoxing")
		@NonNls
		private static final @Immutable Map<String, Function<@Nonnull ICursor, @Nullable ?>> OBJECT_VARIABLE_MAP =
				ImmutableMap.<String, Function<@Nonnull ICursor, @Nullable ?>>builder()
						.put("handle", ICursor::getHandle)
						.build();

		public static @Immutable Map<String, Function<@Nonnull ICursor, @Nullable ?>> getObjectVariableMap() { return OBJECT_VARIABLE_MAP; }
	}
}
