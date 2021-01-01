package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.io.def;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
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
		@SuppressWarnings("AutoBoxing")
		@NonNls
		private static final ImmutableMap<String, Function<@Nonnull IMouseButtonClickData, @Nullable ?>> OBJECT_VARIABLE_MAP =
				ImmutableMap.<String, Function<@Nonnull IMouseButtonClickData, @Nullable ?>>builder()
						.put("cursorPositionView", IMouseButtonClickData::getCursorPositionView)
						.put("button", IMouseButtonClickData::getButton)
						.build();

		public static int getMouseButtonNull() { return MOUSE_BUTTON_NULL; }

		public static ImmutableMap<String, Function<@Nonnull IMouseButtonClickData, @Nullable ?>> getObjectVariableMap() { return OBJECT_VARIABLE_MAP; }
	}
}
