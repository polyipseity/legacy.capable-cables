package $group__.client.ui.core.structures.shapes;

import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.client.ui.structures.EnumUISide;
import $group__.utilities.MapUtilities;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.ConcurrentModificationException;
import java.util.function.Function;

// TODO messy, needs improvement
public interface IUIAnchor {
	ImmutableList<Function<? super IUIAnchor, ?>> OBJECT_VARIABLES = ImmutableList.of(
			IUIAnchor::getTarget, IUIAnchor::getOriginSide, IUIAnchor::getTargetSide, IUIAnchor::getBorderThickness);
	ImmutableMap<String, Function<? super IUIAnchor, ?>> OBJECT_VARIABLES_MAP = ImmutableMap.copyOf(MapUtilities.stitchIterables(OBJECT_VARIABLES.size(),
			ImmutableList.of("target", "originSide", "targetSide", "borderThickness"), OBJECT_VARIABLES));

	boolean isAnchoring();

	IUIComponent getTarget();

	EnumUISide getOriginSide();

	EnumUISide getTargetSide();

	double getBorderThickness();

	void anchor(IShapeDescriptor<?> from)
			throws ConcurrentModificationException;

	void onContainerAdded(IUIAnchorSet<?> container);

	void onContainerRemoved();
}
