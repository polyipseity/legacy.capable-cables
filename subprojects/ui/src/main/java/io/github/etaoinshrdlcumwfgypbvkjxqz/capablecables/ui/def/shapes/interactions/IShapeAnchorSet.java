package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.shapes.interactions;

import com.google.common.collect.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUISide;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map;

public interface IShapeAnchorSet {
	boolean addAnchors(Iterator<? extends IShapeAnchor> anchors);

	@SuppressWarnings("UnstableApiUsage")
	default boolean removeAnchors(Iterator<? extends IShapeAnchor> anchors) {
		Map<? extends EnumUISide, ? extends IShapeAnchor> as = getAnchorsView();
		return removeSides(Streams.stream(anchors)
				.filter(as::containsValue)
				.map(IShapeAnchor::getOriginSide)
				.iterator());
	}

	Map<? extends EnumUISide, ? extends IShapeAnchor> getAnchorsView();

	boolean removeSides(Iterator<? extends EnumUISide> sides);

	default void anchor(IShapeDescriptorProvider from) { getAnchorsView().values().stream().unordered().forEach(v -> v.anchor(from)); }

	default boolean isEmpty() { return getAnchorsView().isEmpty(); }

	default boolean clear() { return removeSides(EnumSet.allOf(EnumUISide.class).iterator()); }

	enum StaticHolder {
		;

		@SuppressWarnings("UnstableApiUsage")
		private static final @Immutable Map<EnumUISide, ImmutableSet<EnumUISide>> EXCLUSIVE_SIDES_MAP = Maps.immutableEnumMap(
				ImmutableMap.<EnumUISide, ImmutableSet<EnumUISide>>builder()
						.put(EnumUISide.UP, Sets.immutableEnumSet(EnumUISide.VERTICAL))
						.put(EnumUISide.DOWN, Sets.immutableEnumSet(EnumUISide.VERTICAL))
						.put(EnumUISide.VERTICAL, Sets.immutableEnumSet(EnumUISide.UP, EnumUISide.DOWN))
						.put(EnumUISide.LEFT, Sets.immutableEnumSet(EnumUISide.HORIZONTAL))
						.put(EnumUISide.RIGHT, Sets.immutableEnumSet(EnumUISide.HORIZONTAL))
						.put(EnumUISide.HORIZONTAL, Sets.immutableEnumSet(EnumUISide.LEFT, EnumUISide.RIGHT))
						.build()
		);

		public static @Immutable Map<EnumUISide, ImmutableSet<EnumUISide>> getExclusiveSidesMap() {
			return EXCLUSIVE_SIDES_MAP;
		}
	}
}
