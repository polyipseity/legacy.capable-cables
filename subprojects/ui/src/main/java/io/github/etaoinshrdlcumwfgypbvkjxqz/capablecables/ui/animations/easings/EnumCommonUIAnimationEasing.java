package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.animations.easings;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationEasing;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.DoubleUnaryOperator;

import static java.lang.Math.*;

// COMMENT https://easings.net
@Immutable
public enum EnumCommonUIAnimationEasing
		implements IUIAnimationEasing {
	LINEAR(DoubleUnaryOperator.identity()::applyAsDouble),
	IN_SINE(x -> 1 - cos((x * PI) / 2)),
	OUT_SINE(x -> sin((x * PI) / 2)),
	IN_OUT_SINE(x -> -(cos(PI * x) - 1) / 2),
	IN_QUAD(x -> x * x),
	OUT_QUAD(x -> 1 - (1 - x) * (1 - x)),
	IN_OUT_QUAD(x -> x < 0.5 ? 2 * x * x : 1 - pow(-2 * x + 2, 2) / 2),
	IN_CUBIC(x -> x * x * x),
	OUT_CUBIC(x -> 1 - pow(1 - x, 3)),
	IN_OUT_CUBIC(x -> x < 0.5 ? 4 * x * x * x : 1 - pow(-2 * x + 2, 3) / 2),
	IN_QUART(x -> x * x * x * x),
	OUT_QUART(x -> 1 - pow(1 - x, 4)),
	IN_OUT_QUART(x -> x < 0.5 ? 8 * x * x * x * x : 1 - pow(-2 * x + 2, 4) / 2),
	IN_QUINT(x -> x * x * x * x * x),
	OUT_QUINT(x -> 1 - pow(1 - x, 5)),
	IN_OUT_QUINT(x -> x < 0.5 ? 16 * x * x * x * x * x : 1 - pow(-2 * x + 2, 5) / 2),
	IN_EXPO(x -> x == 0 ? 0 : pow(2, 10 * x - 10)),
	OUT_EXPO(x -> x == 1 ? 1 : 1 - pow(2, -10 * x)),
	IN_OUT_EXPO(x -> x == 0
			? 0
			: x == 1
			? 1
			: x < 0.5 ? pow(2, 20 * x - 10) / 2
			: (2 - pow(2, -20 * x + 10)) / 2),
	@SuppressWarnings("SpellCheckingInspection") IN_CIRC(x -> 1 - sqrt(1 - pow(x, 2))),
	@SuppressWarnings("SpellCheckingInspection") OUT_CRIC(x -> sqrt(1 - pow(x - 1, 2))),
	@SuppressWarnings("SpellCheckingInspection") IN_OUT_CRIC(x -> x < 0.5
			? (1 - sqrt(1 - pow(2 * x, 2))) / 2
			: (sqrt(1 - pow(-2 * x + 2, 2)) + 1) / 2),
	IN_BACK {
		private static final double c1 = 1.70158;
		private static final double c3 = c1 + 1;

		@Override
		public double applyAsDouble(double x) {
			return c3 * x * x * x - c1 * x * x;
		}
	},
	OUT_BACK {
		private static final double c1 = 1.70158;
		private static final double c3 = c1 + 1;

		@Override
		public double applyAsDouble(double x) {
			return 1 + c3 * pow(x - 1, 3) + c1 * pow(x - 1, 2);
		}
	},
	IN_OUT_BACK {
		private static final double c1 = 1.70158;
		private static final double c2 = c1 * 1.525;

		@Override
		public double applyAsDouble(double x) {
			return x < 0.5
					? (pow(2 * x, 2) * ((c2 + 1) * 2 * x - c2)) / 2
					: (pow(2 * x - 2, 2) * ((c2 + 1) * (x * 2 - 2) + c2) + 2) / 2;
		}
	},
	IN_ELASTIC {
		private static final double c4 = (2 * PI) / 3;

		@Override
		public double applyAsDouble(double x) {
			return x == 0
					? 0
					: x == 1
					? 1
					: -pow(2, 10 * x - 10) * sin((x * 10 - 10.75) * c4);
		}
	},
	OUT_ELASTIC {
		private static final double c4 = (2 * PI) / 3;

		@Override
		public double applyAsDouble(double x) {
			return x == 0
					? 0
					: x == 1
					? 1
					: pow(2, -10 * x) * sin((x * 10 - 0.75) * c4) + 1;
		}
	},
	IN_OUT_ELASTIC {
		private static final double c5 = (2 * PI) / 4.5;

		@Override
		public double applyAsDouble(double x) {
			return x == 0
					? 0
					: x == 1
					? 1
					: x < 0.5
					? -(pow(2, 20 * x - 10) * sin((20 * x - 11.125) * c5)) / 2
					: (pow(2, -20 * x + 10) * sin((20 * x - 11.125) * c5)) / 2 + 1;
		}
	},
	IN_BOUNCE {
		@Override
		public double applyAsDouble(double x) {
			return 1 - OUT_BOUNCE.applyAsDouble(1 - x);
		}
	},
	OUT_BOUNCE {
		private static final double n1 = 7.5625;
		private static final double d1 = 2.75;

		@SuppressWarnings("MagicNumber")
		@Override
		public double applyAsDouble(double x) {
			if (x < 1 / d1) {
				return n1 * x * x;
			} else if (x < 2 / d1) {
				return n1 * (x -= 1.5 / d1) * x + 0.75;
			} else if (x < 2.5 / d1) {
				return n1 * (x -= 2.25 / d1) * x + 0.9375;
			} else {
				return n1 * (x -= 2.625 / d1) * x + 0.984375;
			}
		}
	},
	IN_OUT_BOUNCE {
		@Override
		public double applyAsDouble(double x) {
			return x < 0.5
					? (1 - OUT_BOUNCE.applyAsDouble(1 - 2 * x)) / 2
					: (1 + OUT_BOUNCE.applyAsDouble(2 * x - 1)) / 2;
		}
	},
	;

	@Nullable
	private final IUIAnimationEasing delegate;

	EnumCommonUIAnimationEasing() {
		this(null);
	}

	EnumCommonUIAnimationEasing(@Nullable IUIAnimationEasing delegate) {
		this.delegate = delegate;
	}

	@Override
	public double applyAsDouble(double x) { return getDelegate().applyAsDouble(x); }

	protected IUIAnimationEasing getDelegate() {
		return Objects.requireNonNull(delegate);
	}
}
