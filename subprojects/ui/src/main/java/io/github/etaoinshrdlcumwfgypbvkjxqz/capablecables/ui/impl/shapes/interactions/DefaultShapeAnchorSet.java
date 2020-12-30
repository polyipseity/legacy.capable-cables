package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.interactions;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.shapes.interactions.IShapeAnchor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.shapes.interactions.IShapeAnchorSet;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.shapes.interactions.IShapeDescriptorProvider;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUISide;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.primitives.BooleanUtilities.PaddedBool;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.primitives.BooleanUtilities.PaddedBool.*;

public class DefaultShapeAnchorSet
		implements IShapeAnchorSet {
	private final ConcurrentMap<EnumUISide, IShapeAnchor> anchors = MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(EnumUISide.values().length).makeMap();

	public static @Immutable Set<IShapeAnchor> getAnchorsToMatch(IShapeDescriptorProvider target) { return getAnchorsToMatch(target, 0); }

	public static @Immutable Set<IShapeAnchor> getAnchorsToMatch(IShapeDescriptorProvider target, double borderThickness) {
		return ImmutableSet.of(
				new ImmutableShapeAnchor(target, EnumUISide.UP, EnumUISide.DOWN, borderThickness),
				new ImmutableShapeAnchor(target, EnumUISide.DOWN, EnumUISide.UP, borderThickness),
				new ImmutableShapeAnchor(target, EnumUISide.LEFT, EnumUISide.RIGHT, borderThickness),
				new ImmutableShapeAnchor(target, EnumUISide.RIGHT, EnumUISide.LEFT, borderThickness));
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public boolean addAnchors(Iterator<? extends IShapeAnchor> anchors) {
		return stripBool(
				Streams.stream(anchors)
						.peek(anchor -> {
							removeSides(AssertionUtilities.assertNonnull(StaticHolder.getExclusiveSidesMap().get(anchor.getOriginSide())).iterator());
							getAnchors().put(anchor.getOriginSide(), anchor);
							anchor.onContainerAdded(this);
						})
						.mapToInt(PaddedBoolStreams::tBool)
						.reduce(fBool(), PaddedBool::orBool)
		);
	}

	@Override
	public Map<? extends EnumUISide, ? extends IShapeAnchor> getAnchorsView() { return ImmutableMap.copyOf(getAnchors()); }

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public boolean removeSides(Iterator<? extends EnumUISide> sides) {
		return stripBool(
				Streams.stream(sides).unordered()
						.map(getAnchors()::remove)
						.filter(Objects::nonNull)
						.peek(IShapeAnchor::onContainerRemoved)
						.mapToInt(anchor -> padBool(true))
						.reduce(fBool(), PaddedBool::orBool)
		);
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<EnumUISide, IShapeAnchor> getAnchors() { return anchors; }
}
