package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.ICloneable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.ICopyable;
import org.jetbrains.annotations.NonNls;

import javax.annotation.concurrent.Immutable;
import java.util.function.Function;

@Immutable
public interface IUIKeyboardKeyPressData
		extends ICloneable, ICopyable {

	int getKey();

	int getScanCode();

	int getModifiers();

	long getTimestampMills();

	IUIKeyboardKeyPressData recreate();

	enum StaticHolder {
		;

		private static final ImmutableList<Function<? super IUIKeyboardKeyPressData, ?>> OBJECT_VARIABLES = ImmutableList.of(
				IUIKeyboardKeyPressData::getKey, IUIKeyboardKeyPressData::getScanCode, IUIKeyboardKeyPressData::getModifiers, IUIKeyboardKeyPressData::getTimestampMills);
		@NonNls
		private static final ImmutableMap<String, Function<? super IUIKeyboardKeyPressData, ?>> OBJECT_VARIABLES_MAP = ImmutableMap.copyOf(MapUtilities.zipKeysValues(
				ImmutableList.of("key", "scanCode", "modifiers", "timestampMills"),
				getObjectVariables()));

		public static ImmutableList<Function<? super IUIKeyboardKeyPressData, ?>> getObjectVariables() { return OBJECT_VARIABLES; }

		public static ImmutableMap<String, Function<? super IUIKeyboardKeyPressData, ?>> getObjectVariablesMap() { return OBJECT_VARIABLES_MAP; }
	}
}
