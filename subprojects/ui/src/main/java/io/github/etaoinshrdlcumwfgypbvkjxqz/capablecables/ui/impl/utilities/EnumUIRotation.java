package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;

import java.util.List;
import java.util.Optional;

public enum EnumUIRotation {
	CLOCKWISE {
		@Override
		public EnumUIRotation getOpposite() {
			return COUNTERCLOCKWISE;
		}

		@Override
		public Optional<? extends EnumUISide> rotateBy(EnumUISide side, long quadrants) {
			@Immutable List<EnumUISide> sides = getSidesInClockwiseOrderView();

			long index = sides.indexOf(side);
			if (index == -1L)
				return Optional.empty();

			long size = sides.size();
			return Optional.of(AssertionUtilities.assertNonnull(
					sides.get(Math.toIntExact(
							Math.floorMod(index
											+ Math.floorMod(quadrants, size) /* COMMENT prevent (unlikely) overflow and underflow */,
									size)
					))
			));
		}
	},
	COUNTERCLOCKWISE {
		@Override
		public EnumUIRotation getOpposite() {
			return CLOCKWISE;
		}

		@Override
		public Optional<? extends EnumUISide> rotateBy(EnumUISide side, long quadrants) {
			@Immutable List<EnumUISide> sides = getSidesInClockwiseOrderView();

			long index = sides.indexOf(side);
			if (index == -1L)
				return Optional.empty();

			long size = sides.size();
			return Optional.of(AssertionUtilities.assertNonnull(
					sides.get(Math.toIntExact(
							Math.floorMod(index
											- Math.floorMod(quadrants, size) /* COMMENT prevent (unlikely) overflow and underflow */,
									size)
					))
			));
		}
	},
	;

	private static final List<EnumUISide> SIDES_IN_CLOCKWISE_ORDER = ImmutableList.of(
			EnumUISide.UP,
			EnumUISide.RIGHT,
			EnumUISide.DOWN,
			EnumUISide.LEFT
	);

	public static @Immutable List<EnumUISide> getSidesInClockwiseOrderView() {
		return ImmutableList.copyOf(getSidesInClockwiseOrder());
	}

	private static List<EnumUISide> getSidesInClockwiseOrder() {
		return SIDES_IN_CLOCKWISE_ORDER;
	}

	public abstract EnumUIRotation getOpposite();

	public abstract Optional<? extends EnumUISide> rotateBy(EnumUISide side, long quadrants);
}
