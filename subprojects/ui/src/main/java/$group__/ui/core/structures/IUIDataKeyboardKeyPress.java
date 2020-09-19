package $group__.ui.core.structures;

import $group__.utilities.collections.MapUtilities;
import $group__.utilities.interfaces.ICloneable;
import $group__.utilities.interfaces.ICopyable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import jdk.nashorn.internal.ir.annotations.Immutable;
import org.jetbrains.annotations.NonNls;

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
