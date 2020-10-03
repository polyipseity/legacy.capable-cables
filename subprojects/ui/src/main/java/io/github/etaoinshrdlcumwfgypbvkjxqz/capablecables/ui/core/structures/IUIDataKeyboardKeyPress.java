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
public interface IUIDataKeyboardKeyPress
		extends ICloneable, ICopyable {

	int getKey();

	int getScanCode();

	int getModifiers();

	long getTimestampMills();

	IUIDataKeyboardKeyPress recreate();

	enum StaticHolder {
		;

		private static final ImmutableList<Function<? super IUIDataKeyboardKeyPress, ?>> OBJECT_VARIABLES = ImmutableList.of(
				IUIDataKeyboardKeyPress::getKey, IUIDataKeyboardKeyPress::getScanCode, IUIDataKeyboardKeyPress::getModifiers, IUIDataKeyboardKeyPress::getTimestampMills);
		@NonNls
		private static final ImmutableMap<String, Function<? super IUIDataKeyboardKeyPress, ?>> OBJECT_VARIABLES_MAP = ImmutableMap.copyOf(MapUtilities.stitchKeysValues(getObjectVariables().size(),
				ImmutableList.of("key", "scanCode", "modifiers", "timestampMills"),
				getObjectVariables()));

		public static ImmutableList<Function<? super IUIDataKeyboardKeyPress, ?>> getObjectVariables() { return OBJECT_VARIABLES; }

		public static ImmutableMap<String, Function<? super IUIDataKeyboardKeyPress, ?>> getObjectVariablesMap() { return OBJECT_VARIABLES_MAP; }
	}
}
