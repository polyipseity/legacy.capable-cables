package $group__.ui.core.structures;

import $group__.utilities.collections.MapUtilities;
import $group__.utilities.interfaces.ICopyable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import jdk.nashorn.internal.ir.annotations.Immutable;

import java.awt.geom.Point2D;
import java.util.function.Function;

@Immutable
public interface IUIDataMouseButtonClick
		extends ICopyable {
	int MOUSE_BUTTON_NULL = -1;
	ImmutableList<Function<? super IUIDataMouseButtonClick, ?>> OBJECT_VARIABLES = ImmutableList.of(
			IUIDataMouseButtonClick::getCursorPositionView, IUIDataMouseButtonClick::getButton, IUIDataMouseButtonClick::getTimestampMills);
	ImmutableMap<String, Function<? super IUIDataMouseButtonClick, ?>> OBJECT_VARIABLES_MAP = ImmutableMap.copyOf(MapUtilities.stitchKeysValues(OBJECT_VARIABLES.size(),
			ImmutableList.of("cursorPositionView", "button", "timestampMills"),
			OBJECT_VARIABLES));

	Point2D getCursorPositionView();

	int getButton();

	long getTimestampMills();

	IUIDataMouseButtonClick recreate();
}
