package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.interactions;

import com.google.common.collect.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.utilities.EnumUISide;

import java.util.EnumSet;
import java.util.Map;

public interface IShapeAnchorSet {
	ImmutableMap<EnumUISide, ImmutableSet<EnumUISide>> EXCLUSIVE_SIDES_MAP = ImmutableMap.<EnumUISide, ImmutableSet<EnumUISide>>builder()
			.put(EnumUISide.UP, Sets.immutableEnumSet(EnumUISide.VERTICAL))
			.put(EnumUISide.DOWN, Sets.immutableEnumSet(EnumUISide.VERTICAL))
			.put(EnumUISide.VERTICAL, Sets.immutableEnumSet(EnumUISide.UP, EnumUISide.DOWN))
			.put(EnumUISide.LEFT, Sets.immutableEnumSet(EnumUISide.HORIZONTAL))
			.put(EnumUISide.RIGHT, Sets.immutableEnumSet(EnumUISide.HORIZONTAL))
			.put(EnumUISide.HORIZONTAL, Sets.immutableEnumSet(EnumUISide.LEFT, EnumUISide.RIGHT)).build();

	boolean addAnchors(Iterable<? extends IShapeAnchor> anchors);

	@SuppressWarnings("UnstableApiUsage")
	default boolean removeAnchors(Iterable<? extends IShapeAnchor> anchors) {
		Map<? extends EnumUISide, ? extends IShapeAnchor> as = getAnchorsView();
		return removeSides(Streams.stream(anchors).sequential()
				.filter(as::containsValue)
				.map(IShapeAnchor::getOriginSide)
				.collect(ImmutableList.toImmutableList()));
	}

	Map<? extends EnumUISide, ? extends IShapeAnchor> getAnchorsView();

	boolean removeSides(Iterable<? extends EnumUISide> sides);

	default void anchor(IShapeDescriptorProvider from) { getAnchorsView().values().stream().unordered().forEach(v -> v.anchor(from)); }

	default boolean isEmpty() { return getAnchorsView().isEmpty(); }

	default boolean clear() { return removeSides(EnumSet.allOf(EnumUISide.class)); }
}
