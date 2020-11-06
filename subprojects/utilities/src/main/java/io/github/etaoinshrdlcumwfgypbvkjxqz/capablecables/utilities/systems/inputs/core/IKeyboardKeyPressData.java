package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.ICloneable;
import org.jetbrains.annotations.NonNls;

import javax.annotation.concurrent.Immutable;
import java.util.function.Function;

@Immutable
public interface IKeyboardKeyPressData
		extends ICloneable, ITimestampedInputData {
	int getKey();

	int getScanCode();

	int getModifiers();

	@Override
	IKeyboardKeyPressData recreate();

	@Override
	IKeyboardKeyPressData clone();

	enum StaticHolder {
		;

		@NonNls
		private static final ImmutableMap<String, Function<@Nonnull IKeyboardKeyPressData, @Nullable ?>> OBJECT_VARIABLE_MAP =
				ImmutableMap.<String, Function<@Nonnull IKeyboardKeyPressData, @Nullable ?>>builder()
						.put("key", IKeyboardKeyPressData::getKey)
						.put("scanCode", IKeyboardKeyPressData::getScanCode)
						.put("modifiers", IKeyboardKeyPressData::getModifiers)
						.build();

		public static ImmutableMap<String, Function<@Nonnull IKeyboardKeyPressData, @Nullable ?>> getObjectVariableMap() { return OBJECT_VARIABLE_MAP; }
	}
}
