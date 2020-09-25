package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ui;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AffineTransformUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IConsumer4;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IFunction4;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static java.lang.Math.max;
import static java.lang.Math.min;

public enum UIObjectUtilities {
	;

	public static Shape copyShape(Shape shape) { return AffineTransformUtilities.getIdentity().createTransformedShape(shape); }

	public static Point2D[] getDiagonalsFromRectangular(RectangularShape rectangular) {
		return new Point2D[]{
				new Point2D.Double(rectangular.getX(), rectangular.getY()),
				new Point2D.Double(rectangular.getMaxX(), rectangular.getMaxY())
		};
	}

	public static <R extends Dimension2D> R floorDimension(Dimension2D source, R destination) {
		acceptDimension(source, (x, y) -> destination.setSize(Math.floor(x), Math.floor(y)));
		return destination;
	}

	public static <R extends RectangularShape> R floorRectangularShape(RectangularShape source, R destination) {
		acceptRectangularShape(source, (x, y, w, h) -> destination.setFrame(Math.floor(x), Math.floor(y), Math.floor(w), Math.floor(h)));
		return destination;
	}

	public static <TH extends Throwable> void acceptRectangularShape(RectangularShape rectangular, IConsumer4<? super Double, ? super Double, ? super Double, ? super Double, ? extends TH> action)
			throws TH { action.accept(rectangular.getX(), rectangular.getY(), rectangular.getWidth(), rectangular.getHeight()); }

	public static void acceptPoint(Point2D point, BiConsumer<? super Double, ? super Double> action) { action.accept(point.getX(), point.getY()); }

	public static void acceptDimension(Dimension2D dimension, BiConsumer<? super Double, ? super Double> action) { action.accept(dimension.getWidth(), dimension.getHeight()); }

	public static <R extends Point2D> R floorPoint(Point2D source, R destination) {
		acceptPoint(source, (x, y) -> destination.setLocation(Math.floor(x), Math.floor(y)));
		return destination;
	}

	public static <R extends Point2D> R minPoint(Point2D a, Point2D b, R destination) {
		acceptPoint(a, (ax, ay) -> acceptPoint(b, (bx, by) -> destination.setLocation(min(ax, bx), min(ay, by))));
		return destination;
	}

	public static <R extends Point2D> R maxPoint(Point2D a, Point2D b, R destination) {
		acceptPoint(a, (ax, ay) -> acceptPoint(b, (bx, by) -> destination.setLocation(max(ax, bx), max(ay, by))));
		return destination;
	}

	public static <T> T applyPoint(Point2D point, BiFunction<? super Double, ? super Double, ? extends T> action) { return action.apply(point.getX(), point.getY()); }

	public static <T> T applyDimension(Dimension2D dimension, BiFunction<? super Double, ? super Double, ? extends T> action) { return action.apply(dimension.getWidth(), dimension.getHeight()); }

	public static <R extends RectangularShape> R transformRectangularShape(AffineTransform transform,
	                                                                       RectangularShape rectangular,
	                                                                       R destination) {
		UIObjectUtilities.acceptRectangularShape(rectangular, (x, y, h, w) ->
				destination.setFrameFromDiagonal(x + transform.getTranslateX(), y + transform.getTranslateY(),
						w * transform.getScaleX(), h * transform.getScaleY()));
		return destination;
	}

	public static <T, TH extends Throwable> T applyRectangularShape(RectangularShape rectangular, IFunction4<? super Double, ? super Double, ? super Double, ? super Double, ? extends T, ? extends TH> action)
			throws TH { return action.apply(rectangular.getX(), rectangular.getY(), rectangular.getWidth(), rectangular.getHeight()); }
}
