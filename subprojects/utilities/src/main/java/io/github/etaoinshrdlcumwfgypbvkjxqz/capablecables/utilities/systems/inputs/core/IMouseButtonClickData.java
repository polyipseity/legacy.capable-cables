package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.ICopyable;
import org.jetbrains.annotations.NonNls;

import javax.annotation.concurrent.Immutable;
import java.awt.geom.Point2D;
import java.util.function.Function;

@Immutable
public interface IMouseButtonClickData
		extends ICopyable {

	Point2D getCursorPositionView();

	int getButton();

	long getTimestampMills();

	IMouseButtonClickData recreate();

	enum StaticHolder {
		;

		public static final int MOUSE_BUTTON_NULL = -1;
		private static final ImmutableList<Function<? super IMouseButtonClickData, ?>> OBJECT_VARIABLES = ImmutableList.of(
				IMouseButtonClickData::getCursorPositionView, IMouseButtonClickData::getButton, IMouseButtonClickData::getTimestampMills);
		@NonNls
		private static final ImmutableMap<String, Function<? super IMouseButtonClickData, ?>> OBJECT_VARIABLES_MAP = ImmutableMap.copyOf(MapUtilities.zipKeysValues(
				ImmutableList.of("cursorPositionView", "button", "timestampMills"),
				getObjectVariables()));

		public static int getMouseButtonNull() { return MOUSE_BUTTON_NULL; }

		public static ImmutableList<Function<? super IMouseButtonClickData, ?>> getObjectVariables() { return OBJECT_VARIABLES; }

		public static ImmutableMap<String, Function<? super IMouseButtonClickData, ?>> getObjectVariablesMap() { return OBJECT_VARIABLES_MAP; }
	}
}
