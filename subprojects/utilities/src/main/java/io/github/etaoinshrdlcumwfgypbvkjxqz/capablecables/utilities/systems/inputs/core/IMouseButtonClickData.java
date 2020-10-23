package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.ICloneable;
import org.jetbrains.annotations.NonNls;

import javax.annotation.concurrent.Immutable;
import java.awt.geom.Point2D;
import java.util.function.Function;

@Immutable
public interface IMouseButtonClickData
		extends ICloneable, ITimestampedInputData {
	Point2D getCursorPositionView();

	int getButton();

	@Override
	IMouseButtonClickData recreate();

	@Override
	IMouseButtonClickData clone();

	enum StaticHolder {
		;
		public static final int MOUSE_BUTTON_NULL = -1;
		@NonNls
		private static final ImmutableMap<String, Function<IMouseButtonClickData, ?>> OBJECT_VARIABLES_MAP =
				ImmutableMap.<String, Function<IMouseButtonClickData, ?>>builder()
						.put("cursorPositionView", IMouseButtonClickData::getCursorPositionView)
						.put("button", IMouseButtonClickData::getButton)
						.build();

		public static int getMouseButtonNull() { return MOUSE_BUTTON_NULL; }

		public static ImmutableMap<String, Function<IMouseButtonClickData, ?>> getObjectVariablesMap() { return OBJECT_VARIABLES_MAP; }
	}
}
