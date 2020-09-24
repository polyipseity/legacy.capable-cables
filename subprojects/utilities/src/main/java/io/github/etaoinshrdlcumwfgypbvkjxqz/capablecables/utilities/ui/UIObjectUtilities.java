package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ui;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AffineTransformUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IConsumer4;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IFunction4;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.ImmutableDimension2D;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.ImmutablePoint2D;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.ImmutableRectangle2D;

import java.awt.*;
import java.awt.geom.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static java.lang.Math.max;
import static java.lang.Math.min;

public enum UIObjectUtilities {
	;

	public static Shape copyShape(Shape shape) { return AffineTransformUtilities.getIdentity().createTransformedShape(shape); }

	public static ImmutableDimension2D getDimensionFloor(Dimension2D dimension) { return ImmutableDimension2D.of(Math.floor(dimension.getWidth()), Math.floor(dimension.getHeight())); }

	public static ImmutableRectangle2D getRectangleExpanded(Rectangle2D rectangle) {
		return ImmutableRectangle2D.fromDiagonal(getPointFloor(ImmutablePoint2D.of(rectangle.getX(), rectangle.getY())),
				getPointCeil(ImmutablePoint2D.of(rectangle.getMaxX(), rectangle.getMaxY())));
	}

	public static ImmutablePoint2D getPointFloor(Point2D point) { return ImmutablePoint2D.of(Math.floor(point.getX()), Math.floor(point.getY())); }

	public static ImmutablePoint2D getPointCeil(Point2D point) { return ImmutablePoint2D.of(Math.ceil(point.getX()), Math.ceil(point.getY())); }

	public static void acceptPoint(Point2D point, BiConsumer<? super Double, ? super Double> action) { action.accept(point.getX(), point.getY()); }

	public static void acceptDimension(Dimension2D dimension, BiConsumer<? super Double, ? super Double> action) { action.accept(dimension.getWidth(), dimension.getHeight()); }

	public static <TH extends Throwable> void acceptRectangular(RectangularShape rectangular, IConsumer4<? super Double, ? super Double, ? super Double, ? super Double, ? extends TH> action)
			throws TH { action.accept(rectangular.getX(), rectangular.getY(), rectangular.getWidth(), rectangular.getHeight()); }

	public static ImmutablePoint2D minPoint(Point2D a, Point2D b) { return applyPoint(a, (ax, ay) -> applyPoint(b, (bx, by) -> ImmutablePoint2D.of(min(ax, bx), min(ay, by)))); }

	public static <T> T applyPoint(Point2D point, BiFunction<? super Double, ? super Double, ? extends T> action) { return action.apply(point.getX(), point.getY()); }

	public static ImmutablePoint2D maxPoint(Point2D a, Point2D b) { return applyPoint(a, (ax, ay) -> applyPoint(b, (bx, by) -> ImmutablePoint2D.of(max(ax, bx), max(ay, by)))); }

	public static <T> T applyDimension(Dimension2D dimension, BiFunction<? super Double, ? super Double, ? extends T> action) { return action.apply(dimension.getWidth(), dimension.getHeight()); }

	public static <T, TH extends Throwable> T transformRectangular(AffineTransform transform,
	                                                               RectangularShape rectangular,
	                                                               IFunction4<? super Double, ? super Double, ? super Double, ? super Double, ? extends T, ? extends TH> constructor)
			throws TH {
		return UIObjectUtilities.applyRectangular(rectangular, (x, y, h, w) ->
				constructor.apply(x + transform.getTranslateX(), y + transform.getTranslateY(),
						w * transform.getScaleX(), h * transform.getScaleY()));
	}

	public static <T, TH extends Throwable> T applyRectangular(RectangularShape rectangular, IFunction4<? super Double, ? super Double, ? super Double, ? super Double, ? extends T, ? extends TH> action)
			throws TH { return action.apply(rectangular.getX(), rectangular.getY(), rectangular.getWidth(), rectangular.getHeight()); }
}
