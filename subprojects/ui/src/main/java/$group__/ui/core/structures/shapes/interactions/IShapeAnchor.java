package $group__.ui.core.structures.shapes.interactions;

import $group__.ui.structures.EnumUISide;
import $group__.utilities.collections.MapUtilities;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.ConcurrentModificationException;
import java.util.Optional;
import java.util.function.Function;

// TODO messy, needs improvement
public interface IShapeAnchor {
	ImmutableList<Function<? super IShapeAnchor, ?>> OBJECT_VARIABLES = ImmutableList.of(
			IShapeAnchor::getTarget, IShapeAnchor::getOriginSide, IShapeAnchor::getTargetSide, IShapeAnchor::getBorderThickness, IShapeAnchor::getContainer);
	ImmutableMap<String, Function<? super IShapeAnchor, ?>> OBJECT_VARIABLES_MAP = ImmutableMap.copyOf(MapUtilities.stitchIterables(OBJECT_VARIABLES.size(),
			ImmutableList.of("target", "originSide", "targetSide", "borderThickness", "container"), OBJECT_VARIABLES));

	Optional<? extends IShapeDescriptorProvider> getTarget();

	EnumUISide getOriginSide();

	EnumUISide getTargetSide();

	double getBorderThickness();

	Optional<? extends IShapeAnchorSet> getContainer();

	void anchor(IShapeDescriptorProvider from)
			throws ConcurrentModificationException;

	void onContainerAdded(IShapeAnchorSet container);

	void onContainerRemoved();
}
