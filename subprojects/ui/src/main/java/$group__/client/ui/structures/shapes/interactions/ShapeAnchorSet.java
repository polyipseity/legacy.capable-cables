package $group__.client.ui.structures.shapes.interactions;

import $group__.client.ui.core.structures.shapes.interactions.IShapeAnchor;
import $group__.client.ui.core.structures.shapes.interactions.IShapeAnchorSet;
import $group__.client.ui.core.structures.shapes.interactions.IShapeDescriptorProvider;
import $group__.client.ui.structures.EnumUISide;
import $group__.utilities.MapUtilities;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public class ShapeAnchorSet
		implements IShapeAnchorSet {
	protected final ConcurrentMap<EnumUISide, IShapeAnchor> anchors = MapUtilities.getMapMakerSingleThreaded().initialCapacity(EnumUISide.values().length).makeMap();

	public static Set<IShapeAnchor> getAnchorsToMatch(IShapeDescriptorProvider target) { return getAnchorsToMatch(target, 0); }

	public static Set<IShapeAnchor> getAnchorsToMatch(IShapeDescriptorProvider target, double borderThickness) {
		return Sets.newHashSet(
				new ShapeAnchor(target, EnumUISide.UP, EnumUISide.DOWN, borderThickness),
				new ShapeAnchor(target, EnumUISide.DOWN, EnumUISide.UP, borderThickness),
				new ShapeAnchor(target, EnumUISide.LEFT, EnumUISide.RIGHT, borderThickness),
				new ShapeAnchor(target, EnumUISide.RIGHT, EnumUISide.LEFT, borderThickness));
	}

	@Override
	public boolean addAnchors(Iterable<? extends IShapeAnchor> anchors) {
		boolean ret = false;
		for (IShapeAnchor anchor : anchors) {
			removeSides(Optional.ofNullable(EXCLUSIVE_SIDES_MAP.get(anchor.getOriginSide()))
					.orElseThrow(InternalError::new));
			getAnchors().put(anchor.getOriginSide(), anchor);
			anchor.onContainerAdded(this);
			ret = true;
		}
		return ret;
	}

	@Override
	public Map<? extends EnumUISide, ? extends IShapeAnchor> getAnchorsView() { return ImmutableMap.copyOf(getAnchors()); }

	@Override
	public boolean removeSides(Iterable<? extends EnumUISide> sides) {
		final boolean[] ret = {false};
		sides.forEach(side -> {
			Optional.ofNullable(getAnchors().remove(side)).ifPresent(IShapeAnchor::onContainerRemoved);
			ret[0] = true;
		});
		return ret[0];
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<EnumUISide, IShapeAnchor> getAnchors() { return anchors; }
}
