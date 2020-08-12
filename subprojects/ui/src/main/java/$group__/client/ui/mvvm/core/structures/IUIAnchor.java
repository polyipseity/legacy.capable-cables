package $group__.client.ui.mvvm.core.structures;

import $group__.client.ui.mvvm.structures.EnumUISide;
import $group__.utilities.specific.MapUtilities;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.function.Function;

public interface IUIAnchor {
	ImmutableList<Function<? super IUIAnchor, ?>> OBJECT_VARIABLES = ImmutableList.of(
			IUIAnchor::getTo, IUIAnchor::getFromSide, IUIAnchor::getToSide, IUIAnchor::getBorderThickness);
	ImmutableMap<String, Function<? super IUIAnchor, ?>> OBJECT_VARIABLES_MAP = ImmutableMap.copyOf(MapUtilities.stitchIterables(OBJECT_VARIABLES.size(),
			ImmutableList.of("to", "fromSide", "toSide", "borderThickness"), OBJECT_VARIABLES));

	IShapeDescriptor<?, ?> getTo();

	EnumUISide getFromSide();

	EnumUISide getToSide();

	double getBorderThickness();

	void anchor(IShapeDescriptor<?, ?> from);

	void onContainerAdded(IUIAnchorSet<?> container);

	void onContainerRemoved();
}
