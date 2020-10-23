package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapUtilities;
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

		private static final ImmutableList<Function<? super IKeyboardKeyPressData, ?>> OBJECT_VARIABLES = ImmutableList.of(
				IKeyboardKeyPressData::getKey, IKeyboardKeyPressData::getScanCode, IKeyboardKeyPressData::getModifiers, IKeyboardKeyPressData::getTimestamp);
		@NonNls
		private static final ImmutableMap<String, Function<? super IKeyboardKeyPressData, ?>> OBJECT_VARIABLES_MAP = ImmutableMap.copyOf(MapUtilities.zipKeysValues(
				ImmutableList.of("key", "scanCode", "modifiers", "timestampMills"),
				getObjectVariables()));

		public static ImmutableList<Function<? super IKeyboardKeyPressData, ?>> getObjectVariables() { return OBJECT_VARIABLES; }

		public static ImmutableMap<String, Function<? super IKeyboardKeyPressData, ?>> getObjectVariablesMap() { return OBJECT_VARIABLES_MAP; }
	}
}
