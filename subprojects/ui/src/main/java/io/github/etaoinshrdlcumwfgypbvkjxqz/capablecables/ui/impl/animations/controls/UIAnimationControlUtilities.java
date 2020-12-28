package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.animations.controls;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationEasing;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.animations.easings.EnumUICommonAnimationEasing;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.core.IFunction3;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.ITuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples.ImmutableTuple2;
import org.jetbrains.annotations.NonNls;

import java.util.Map;
import java.util.function.IntToDoubleFunction;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities.assertNonnull;

public enum UIAnimationControlUtilities {
	;

	@SuppressWarnings({"AutoBoxing", "AutoUnboxing"})
	public static IFunction3<IUIAnimationTarget, Integer, Integer, Double, RuntimeException> stagger(double interval)
			throws IllegalArgumentException {
		// COMMENT return a specialized function
		return (target, index, size) -> interval * assertNonnull(index);
	}

	@SuppressWarnings({"AutoBoxing", "AutoUnboxing"})
	public static IFunction3<IUIAnimationTarget, Integer, Integer, Double, RuntimeException> stagger(double from, double to)
			throws IllegalArgumentException {
		// COMMENT return a specialized function
		return (target, index, size) -> from + (from - to) / assertNonnull(size) * assertNonnull(index);
	}

	@SuppressWarnings("AutoBoxing")
	public static IFunction3<IUIAnimationTarget, Integer, Integer, Double, RuntimeException> stagger(double from, double to, Map<String, Object> options) {
		return stagger(ImmutableTuple2.of(from, to), options);
	}

	@SuppressWarnings({"AutoBoxing", "AutoUnboxing"})
	private static IFunction3<IUIAnimationTarget, Integer, Integer, Double, RuntimeException> stagger(Object value, Map<String, Object> options)
			throws IllegalArgumentException {
		// COMMENT options
		double start = StaggerOptions.parseStart(options);
		IntToDoubleFunction startingPosition = StaggerOptions.parseStartingPosition(options);
		StaggerOptions.EnumDirection direction = StaggerOptions.parseDirection(options);
		IUIAnimationEasing easing = StaggerOptions.parseEasing(options);

		// COMMENT function
		return (target, index, size) -> {
			/* COMMENT
			 * The algorithm is pretty lousy.
			 *
			 * Example
			 *
			 * interval = 100
			 * start = 100
			 * startingPosition = size -> size / 2
			 * direction = REVERSE
			 * easing = LINEAR
			 *
			 * target = class
			 * index = 1
			 * size = 5
			 *
			 * rawStartingPosition = 2.5
			 * lowerStartingPosition = 2
			 * upperStartingPosition = 3
			 * maxDiff = 2
			 * nominalDiff = 1
			 * diff = 1
			 * relativeDiff = 0.5
			 * easedRelativeDiff = 0.5
			 *
			 * return 200
			 */

			// COMMENT starting position
			double rawStartingPosition = startingPosition.applyAsDouble(size); // COMMENT may be x.5
			int lowerStartingPosition = (int) Math.floor(rawStartingPosition);
			int upperStartingPosition = (int) Math.ceil(rawStartingPosition);

			// COMMENT difference
			int maxDiff = Math.max(lowerStartingPosition, size - upperStartingPosition);
			int nominalDiff = Math.max(lowerStartingPosition - index, index - upperStartingPosition);
			int diff;
			switch (direction) {
				case NORMAL:
					diff = nominalDiff;
					break;
				case REVERSE:
					diff = maxDiff - nominalDiff;
					break;
				default:
					throw new AssertionError();
			}
			double relativeDiff = (double) diff / maxDiff;
			double easedRelativeDiff = easing.applyAsDouble(relativeDiff);

			// COMMENT interval
			double interval;
			if (value instanceof Number) {
				interval = ((Number) value).doubleValue();
			} else if (value instanceof ITuple2) {
				ITuple2<?, ?> tuple = (ITuple2<?, ?>) value;
				double from = (Double) tuple.getLeft();
				double to = (Double) tuple.getRight();
				interval = (to - from) / maxDiff;
			} else
				throw new IllegalArgumentException();

			// COMMENT result
			return start + interval * maxDiff * easedRelativeDiff;
		};
	}

	@SuppressWarnings("AutoBoxing")
	public static IFunction3<IUIAnimationTarget, Integer, Integer, Double, RuntimeException> stagger(double interval, Map<String, Object> options) {
		return stagger((Object) interval, options);
	}

	public enum StaggerOptions {
		;

		public static final @NonNls String OPTION_START_KEY = "start";
		public static final @NonNls String OPTION_STARTING_POSITION_KEY = "from";
		public static final @NonNls String OPTION_DIRECTION_KEY = "direction";
		public static final @NonNls String OPTION_EASING_KEY = "easing";

		private static double parseStart(Map<String, Object> options)
				throws IllegalArgumentException {
			@Nullable Object value = options.get(getOptionStartKey());
			if (value == null)
				return 0;
			else if (value instanceof Number)
				return ((Number) value).doubleValue();
			throw new IllegalArgumentException();
		}

		public static String getOptionStartKey() { return OPTION_START_KEY; }

		private static IntToDoubleFunction parseStartingPosition(Map<String, Object> options)
				throws IllegalArgumentException {
			@Nullable Object value = options.get(getOptionStartingPositionKey());
			if (value == null) {
				return EnumStartingPosition.FIRST;
			} else if (value instanceof EnumStartingPosition) {
				return (IntToDoubleFunction) value;
			} else if (value instanceof Number) {
				int startingPosition = ((Number) value).intValue();
				return size -> startingPosition;
			}
			throw new IllegalArgumentException();
		}

		public static String getOptionStartingPositionKey() { return OPTION_STARTING_POSITION_KEY; }

		private static EnumDirection parseDirection(Map<String, Object> options)
				throws IllegalArgumentException {
			@Nullable Object value = options.get(getOptionDirectionKey());
			if (value == null)
				return EnumDirection.NORMAL;
			else if (value instanceof EnumDirection)
				return (EnumDirection) value;
			throw new IllegalArgumentException();
		}

		public static String getOptionDirectionKey() { return OPTION_DIRECTION_KEY; }

		private static IUIAnimationEasing parseEasing(Map<String, Object> options)
				throws IllegalArgumentException {
			@Nullable Object value = options.get(getOptionEasingKey());
			if (value == null)
				return EnumUICommonAnimationEasing.LINEAR;
			else if (value instanceof IUIAnimationEasing)
				return (IUIAnimationEasing) value;
			throw new IllegalArgumentException();
		}

		public static String getOptionEasingKey() { return OPTION_EASING_KEY; }

		public enum EnumStartingPosition
				implements IntToDoubleFunction {
			FIRST(size -> 0),
			LAST(size -> size - 1),
			CENTER(size -> size / 2d),
			;

			private final IntToDoubleFunction delegate;

			EnumStartingPosition(IntToDoubleFunction delegate) {
				this.delegate = delegate;
			}

			@Override
			public double applyAsDouble(int value) {
				return getDelegate().applyAsDouble(value);
			}

			protected IntToDoubleFunction getDelegate() {
				return delegate;
			}
		}

		public enum EnumDirection {
			NORMAL,
			REVERSE,
		}
	}
}
