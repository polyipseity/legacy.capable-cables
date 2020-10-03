package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.ICopyable;
import org.jetbrains.annotations.NonNls;

import javax.annotation.concurrent.Immutable;
import java.awt.geom.Point2D;
import java.util.function.Function;

@Immutable
public interface IUIDataMouseButtonClick
		extends ICopyable {

	Point2D getCursorPositionView();

	int getButton();

	long getTimestampMills();

	IUIDataMouseButtonClick recreate();

	enum StaticHolder {
		;

		private static final int MOUSE_BUTTON_NULL = -1;
		private static final ImmutableList<Function<? super IUIDataMouseButtonClick, ?>> OBJECT_VARIABLES = ImmutableList.of(
				IUIDataMouseButtonClick::getCursorPositionView, IUIDataMouseButtonClick::getButton, IUIDataMouseButtonClick::getTimestampMills);
		@NonNls
		private static final ImmutableMap<String, Function<? super IUIDataMouseButtonClick, ?>> OBJECT_VARIABLES_MAP = ImmutableMap.copyOf(MapUtilities.stitchKeysValues(getObjectVariables().size(),
				ImmutableList.of("cursorPositionView", "button", "timestampMills"),
				getObjectVariables()));

		public static int getMouseButtonNull() { return MOUSE_BUTTON_NULL; }

		public static ImmutableList<Function<? super IUIDataMouseButtonClick, ?>> getObjectVariables() { return OBJECT_VARIABLES; }

		public static ImmutableMap<String, Function<? super IUIDataMouseButtonClick, ?>> getObjectVariablesMap() { return OBJECT_VARIABLES_MAP; }
	}
}
