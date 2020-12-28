package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

public enum CapacityUtilities {
	;

	public static final int INITIAL_CAPACITY_SCALING_FACTOR = 4;
	@SuppressWarnings("unused")
	public static final int INITIAL_CAPACITY_NONE = 0;
	public static final int INITIAL_CAPACITY_SINGLE = 1;
	public static final int INITIAL_CAPACITY_TINY = INITIAL_CAPACITY_SINGLE * INITIAL_CAPACITY_SCALING_FACTOR;
	public static final int INITIAL_CAPACITY_SMALL = INITIAL_CAPACITY_TINY * INITIAL_CAPACITY_SCALING_FACTOR;
	public static final int INITIAL_CAPACITY_MEDIUM = INITIAL_CAPACITY_SMALL * INITIAL_CAPACITY_SCALING_FACTOR;
	public static final int INITIAL_CAPACITY_LARGE = INITIAL_CAPACITY_MEDIUM * INITIAL_CAPACITY_SCALING_FACTOR;
	public static final int INITIAL_CAPACITY_ENORMOUS = INITIAL_CAPACITY_LARGE * INITIAL_CAPACITY_SCALING_FACTOR;

	public static int getInitialCapacityScalingFactor() {
		return INITIAL_CAPACITY_SCALING_FACTOR;
	}

	public static int getInitialCapacityNone() {
		return INITIAL_CAPACITY_NONE;
	}

	public static int getInitialCapacitySingle() {
		return INITIAL_CAPACITY_SINGLE;
	}

	public static int getInitialCapacityTiny() {
		return INITIAL_CAPACITY_TINY;
	}

	public static int getInitialCapacityMedium() {
		return INITIAL_CAPACITY_MEDIUM;
	}

	public static int getInitialCapacityLarge() {
		return INITIAL_CAPACITY_LARGE;
	}

	public static int getInitialCapacityEnormous() {
		return INITIAL_CAPACITY_ENORMOUS;
	}

	public static int getInitialCapacitySmall() {
		return INITIAL_CAPACITY_SMALL;
	}
}
