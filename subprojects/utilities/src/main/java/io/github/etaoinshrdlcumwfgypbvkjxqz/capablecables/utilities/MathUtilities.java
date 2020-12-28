package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import com.google.common.collect.ImmutableList;
import com.google.common.math.BigIntegerMath;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;

import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;

public enum MathUtilities {
	;

	@SuppressWarnings({"AutoBoxing", "AutoUnboxing"})
	public static @Nullable Double minNullable(@Nullable Double a, @Nullable Double b) {
		if (a == null && b == null)
			return null;
		else if (a == null)
			a = b;
		else if (b == null)
			b = a;
		return Math.min(a, b);
	}

	@SuppressWarnings({"AutoBoxing", "AutoUnboxing"})
	public static @Nullable Double maxNullable(@Nullable Double a, @Nullable Double b) {
		if (a == null && b == null)
			return null;
		else if (a == null)
			a = b;
		else if (b == null)
			b = a;
		return Math.max(a, b);
	}

	public static long pow2Long(long power) {
		return 1L << power; // COMMENT more performant than pow(2, n)
	}

	public static int pow2Int(long power) {
		return 1 << power; // COMMENT more performant than pow(2, n)
	}

	public static double roundToZero(double value) {
		if (value < 0) {
			return Math.ceil(value);
		}
		return Math.floor(value);
	}

	public static double clamp(double value, double min, double max) {
		return Math.min(Math.max(value, min), max);
	}

	public static long clamp(long value, long min, long max) {
		return Math.min(Math.max(value, min), max);
	}

	public enum Polynomials {
		;

		private static double[] filterRealRoots(double... roots) {
			return Arrays.stream(roots)
					.filter(Double::isFinite)
					.distinct()
					.toArray();
		}

		public static double[] quadraticRealRoots(double a, double b, double c) {
			assert ComparableUtilities.unequalTo(a, 0d); // COMMENT not quadratic

			/* COMMENT
			 * a * x**2 + b * x + c = 0
			 *
			 * Apply the quadratic formula
			 */

			double discriminant = b * b - 4 * a * c;

			if (ComparableUtilities.greaterThan(discriminant, 0d)) {
				// COMMENT 2 real roots
				double sqrtDiscriminant = Math.sqrt(discriminant);
				return new double[]{
						(-b + sqrtDiscriminant) / (2 * a),
						(-b - sqrtDiscriminant) / (2 * a)
				};
			} else if (ComparableUtilities.equalTo(discriminant, 0d)) {
				// COMMENT 1 real root
				// COMMENT discriminant is 0
				return new double[]{-b / (2 * a)};
			} else {
				// COMMENT no real roots
				return ArrayUtilities.getEmptyDoubleArray();
			}
		}

		public static double[] cubicRealRoots(double a, double b, double c, double d) {
			assert ComparableUtilities.unequalTo(a, 0d); // COMMENT not cubic

			/* COMMENT
			 * a * x**3 + b * x**2 + c * x + d = 0
			 *
			 * Change into the depressed form
			 * x = t - (b / 3a)
			 * t**3 + p * t + q = 0
			 *
			 * Find all real roots
			 *
			 * Replace roots with the original roots
			 * x = t - (b / 3a)
			 */

			double p = (3 * a * c - b * b) / 3 * a * a;
			@SuppressWarnings("MagicNumber")
			double q = (2 * b * b * b - 9 * a * b * c + 27 * a * a * d) / 27 * a * a * a;

			@SuppressWarnings("MagicNumber")
			double discriminant = -(4 * p * p * p + 27 * q * q);

			double[] roots;
			if (ComparableUtilities.greaterThan(discriminant, 0d)) {
				// COMMENT 1 real root
				@SuppressWarnings("MagicNumber")
				double sqrt = Math.sqrt(q * q / 4 + p * p * p / 27);
				roots = new double[]{
						Math.cbrt(-q / 2 + sqrt) + Math.cbrt(-q / 2 - sqrt)
				};
			} else if (ComparableUtilities.equalTo(discriminant, 0d)) {
				// COMMENT 2 real roots
				@SuppressWarnings("SpellCheckingInspection")
				double cbrt = Math.cbrt(-q / 2);
				roots = new double[]{
						2 * cbrt,
						-cbrt
				};
			} else {
				// COMMENT 3 real roots
				roots = new double[3];
				for (byte k = 0; k < 3; ++k) {
					roots[k] = 2
							* Math.sqrt(-p / 3)
							* Math.cos((1d / 3) * Math.acos(3 * q / (2 * p) * Math.sqrt(-3 / p)) - 2 * Math.PI * k / 3);
				}
			}

			return Arrays.stream(roots)
					.map(root -> root - b / (3 * a))
					.toArray();
		}
	}

	public enum BezierCurves {
		;

		public static OptionalDouble getQuadraticTForX(double x, Iterable<? extends Point2D> points) {
			@Immutable List<Point2D> pointsList = ImmutableList.copyOf(points);
			assert pointsList.size() == 3;
			return getQuadraticTForX(x,
					AssertionUtilities.assertNonnull(pointsList.get(0)),
					AssertionUtilities.assertNonnull(pointsList.get(1)),
					AssertionUtilities.assertNonnull(pointsList.get(2)));
		}

		public static OptionalDouble getQuadraticTForX(double x, Point2D point0, Point2D point1, Point2D point2) {
			/* COMMENT
			 * Bx(t) = c0x + c1x * t + c2x * t**2
			 * By(t) = c0y + c1y * t + c2y * t**2
			 *
			 * Intersection of B(t) with vertical line x = a
			 * c0x + c1x * t + c2x * t**2 – a = 0
			 * c2x * t**2 + c1x * t + c0x – a = 0
			 * a = c2x, b = c1x, c = c0x - a
			 */
			double[] roots = Polynomials.quadraticRealRoots(point2.getX(), point1.getX(), point0.getX() - x);
			return filterTForXRoots(roots);
		}

		private static OptionalDouble filterTForXRoots(double... roots) {
			double[] result = Arrays.stream(roots)
					.filter(root -> ComparableUtilities.greaterThanOrEqualTo(root, 0d))
					.filter(root -> ComparableUtilities.lessThanOrEqualTo(root, 1d))
					.toArray();
			if (result.length == 1)
				return OptionalDouble.of(result[0]);
			return OptionalDouble.empty();
		}

		public static OptionalDouble getCubicTForX(double x, Iterable<? extends Point2D> points) {
			@Immutable List<Point2D> pointsList = ImmutableList.copyOf(points);
			assert pointsList.size() == 4;
			return getCubicTForX(x,
					AssertionUtilities.assertNonnull(pointsList.get(0)),
					AssertionUtilities.assertNonnull(pointsList.get(1)),
					AssertionUtilities.assertNonnull(pointsList.get(2)),
					AssertionUtilities.assertNonnull(pointsList.get(3)));
		}

		public static OptionalDouble getCubicTForX(double x, Point2D point0, Point2D point1, Point2D point2, Point2D point3) {
			/* COMMENT
			 * Bx(t) = c0x + c1x * t + c2x * t**2 + c3x * t**3
			 * By(t) = c0y + c1y * t + c2y * t**2 + c3y * t**3
			 *
			 * Intersection of B(t) with vertical line x = a
			 * c0x + c1x * t + c2x * t**2 + c3x * t**3 – a = 0
			 * c3x * t**3 + c2x * t**2 + c1x * t + c0x – a = 0
			 * a = c3x, b = c2x, c = c1x, d = c0x - a
			 */
			double[] roots = Polynomials.cubicRealRoots(point3.getX(), point2.getX(), point1.getX(), point0.getX() - x);
			return filterTForXRoots(roots);
		}

		public static Point2D bezierCurveExact(Point2D destination, double t, Iterable<? extends Point2D> points)
				throws ArithmeticException {
			BigDecimal[] point = bezierCurve(t, points);
			destination.setLocation(BigDecimals.doubleValueExact(point[0]), BigDecimals.doubleValueExact(point[1]));
			return destination;
		}

		public static BigDecimal[] bezierCurve(double t, Iterable<? extends Point2D> points) {
			assert t >= 0;
			assert t <= 1;
			final BigDecimal[] ret = {BigDecimal.ZERO, BigDecimal.ZERO}; // COMMENT 0 is x, 1 is y
			{
				@Immutable List<Point2D> pointsList = ImmutableList.copyOf(points);
				final int[] iArray = {0};
				int n = pointsList.size() - 1;
				pointsList.forEach(point -> {
					int i = iArray[0];
					BigDecimal coefficient = new BigDecimal(BigIntegerMath.binomial(n, i))
							.multiply(BigDecimal.valueOf(Math.pow(1 - t, n - i)))
							.multiply(BigDecimal.valueOf(Math.pow(t, i)));
					ret[0] = ret[0].add(coefficient.multiply(BigDecimal.valueOf(point.getX())));
					ret[1] = ret[1].add(coefficient.multiply(BigDecimal.valueOf(point.getY())));
					++iArray[0];
				});
			}
			return ret;
		}

		public static Point2D bezierCurve(Point2D destination, double t, Iterable<? extends Point2D> points) {
			BigDecimal[] point = bezierCurve(t, points);
			destination.setLocation(point[0].doubleValue(), point[1].doubleValue());
			return destination;
		}
	}

	public enum BigDecimals {
		;

		public static double doubleValueExact(BigDecimal self)
				throws ArithmeticException {
			double result = self.doubleValue();
			try {
				if (ComparableUtilities.equalTo(valueOfDouble(self), BigDecimal.valueOf(result)))
					return result;
			} catch (NumberFormatException e) {
				// COMMENT probably +-Infinity
				throw (ArithmeticException) new ArithmeticException().initCause(e);
			}
			throw new ArithmeticException();
		}

		public static BigDecimal valueOfDouble(BigDecimal self) {
			return new BigDecimal(self.toString(), MathContext.DECIMAL64);
		}

		public static float floatValueExact(BigDecimal self)
				throws ArithmeticException {
			float result = self.floatValue();
			try {
				if (ComparableUtilities.equalTo(valueOfFloat(self), BigDecimal.valueOf(result)))
					return result;
			} catch (NumberFormatException e) {
				// COMMENT probably +-Infinity
				throw (ArithmeticException) new ArithmeticException().initCause(e);
			}
			throw new ArithmeticException();
		}

		public static BigDecimal valueOfFloat(BigDecimal self) {
			return new BigDecimal(self.toString(), MathContext.DECIMAL32);
		}
	}
}
