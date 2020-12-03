package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import java.util.concurrent.TimeUnit;

public enum EnumTimeUnit {
	NANOSECOND {
		@Override
		public TimeUnit getStandardTimeUnit() {
			return TimeUnit.NANOSECONDS;
		}

		@Override
		public long getScale() {
			return 1L;
		}
	},
	MICROSECOND {
		@Override
		public TimeUnit getStandardTimeUnit() {
			return TimeUnit.MICROSECONDS;
		}

		@Override
		public long getScale() {
			return 1_000L;
		}
	},
	MILLISECOND {
		@Override
		public TimeUnit getStandardTimeUnit() {
			return TimeUnit.MILLISECONDS;
		}

		@Override
		public long getScale() {
			return 1_000_000L;
		}
	},
	SECOND {
		@Override
		public TimeUnit getStandardTimeUnit() {
			return TimeUnit.SECONDS;
		}

		@Override
		public long getScale() {
			return 1_000_000_000L;
		}
	},
	MINUTE {
		@Override
		public TimeUnit getStandardTimeUnit() {
			return TimeUnit.MINUTES;
		}

		@Override
		public long getScale() {
			return 60_000_000_000L;
		}
	},
	HOUR {
		@Override
		public TimeUnit getStandardTimeUnit() {
			return TimeUnit.HOURS;
		}

		@Override
		public long getScale() {
			return 3_600_000_000_000L;
		}
	},
	DAY {
		@Override
		public TimeUnit getStandardTimeUnit() {
			return TimeUnit.DAYS;
		}

		@Override
		public long getScale() {
			return 86_400_000_000_000L;
		}
	},
	;

	private static final double[][] SCALES;

	static {
		EnumTimeUnit[] values = values();
		int valuesLength = values.length;
		double[][] scales = new double[valuesLength][valuesLength];
		for (int fromIndex = 0; fromIndex < valuesLength; ++fromIndex) {
			long fromScale = values[fromIndex].getScale();
			for (int toIndex = 0; toIndex < valuesLength; ++toIndex) {
				long toScale = values[toIndex].getScale();
				long scale = toScale / fromScale;
				scales[fromIndex][toIndex] = scale;
			}
		}
		SCALES = scales;
	}

	public static double getScale(EnumTimeUnit from, EnumTimeUnit to) {
		return getScales()[from.ordinal()][to.ordinal()];
	}

	private static double[][] getScales() {
		return SCALES;
	}

	public abstract TimeUnit getStandardTimeUnit();

	public abstract long getScale();
}
