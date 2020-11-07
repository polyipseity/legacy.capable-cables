package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.interactions;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.interactions.IShapeAnchor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.interactions.IShapeAnchorSet;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.interactions.IShapeDescriptorProvider;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUISide;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public class DefaultShapeAnchorSet
		implements IShapeAnchorSet {
	private final ConcurrentMap<EnumUISide, IShapeAnchor> anchors = MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(EnumUISide.values().length).makeMap();

	public static Set<IShapeAnchor> getAnchorsToMatch(IShapeDescriptorProvider target) { return getAnchorsToMatch(target, 0); }

	public static Set<IShapeAnchor> getAnchorsToMatch(IShapeDescriptorProvider target, double borderThickness) {
		return Sets.newHashSet(
				new ImmutableShapeAnchor(target, EnumUISide.UP, EnumUISide.DOWN, borderThickness),
				new ImmutableShapeAnchor(target, EnumUISide.DOWN, EnumUISide.UP, borderThickness),
				new ImmutableShapeAnchor(target, EnumUISide.LEFT, EnumUISide.RIGHT, borderThickness),
				new ImmutableShapeAnchor(target, EnumUISide.RIGHT, EnumUISide.LEFT, borderThickness));
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public boolean addAnchors(Iterable<? extends IShapeAnchor> anchors) {
		return Streams.stream(anchors)
				.map(anchor -> {
					removeSides(AssertionUtilities.assertNonnull(StaticHolder.getExclusiveSidesMap().get(anchor.getOriginSide())));
					getAnchors().put(anchor.getOriginSide(), anchor);
					anchor.onContainerAdded(this);
					return true;
				})
				.reduce(false, Boolean::logicalOr);
	}

	@Override
	public Map<? extends EnumUISide, ? extends IShapeAnchor> getAnchorsView() { return ImmutableMap.copyOf(getAnchors()); }

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public boolean removeSides(Iterable<? extends EnumUISide> sides) {
		return Streams.stream(sides).unordered()
				.reduce(false, (r, side) ->
						Optional.ofNullable(getAnchors().remove(side))
								.filter(a -> {
									a.onContainerRemoved();
									return true;
								})
								.isPresent() || r, Boolean::logicalOr);
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<EnumUISide, IShapeAnchor> getAnchors() { return anchors; }
}
