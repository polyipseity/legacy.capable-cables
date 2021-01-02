package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.graphics.impl;

import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AffineTransformUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.def.IConsumer4;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.def.IFunction4;

import java.awt.*;
import java.awt.geom.*;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.lang.Math.max;
import static java.lang.Math.min;

public enum UIObjectUtilities {
	;

	public static Shape copyShape(Shape shape) {
		return AffineTransformUtilities.getIdentity().createTransformedShape(shape);
	}

	@SuppressWarnings("UnstableApiUsage")
	public static Optional<Shape> intersectShapes(Iterator<? extends Shape> shapes) {
		return Streams.stream(shapes).unordered()
				.<Shape>map(Function.identity())
				.reduce((shape1, shape2) -> {
					Area result = new Area(shape1);
					result.intersect(new Area(shape2));
					return result;
				});
	}

	public static <T extends RectangularShape> T unPositionRectangularShape(T source, T destination) {
		destination.setFrame(0D, 0D, source.getWidth(), source.getHeight());
		return destination;
	}

	public static Point2D[] getDiagonalsFromRectangular(RectangularShape rectangular) {
		return new Point2D[]{
				new Point2D.Double(rectangular.getX(), rectangular.getY()),
				new Point2D.Double(rectangular.getMaxX(), rectangular.getMaxY())
		};
	}

	@SuppressWarnings("AutoUnboxing")
	public static <R extends Dimension2D> R floorDimension(Dimension2D source, R destination) {
		acceptDimension(source, (x, y) -> destination.setSize(Math.floor(x), Math.floor(y)));
		return destination;
	}

	@SuppressWarnings("AutoBoxing")
	public static void acceptDimension(Dimension2D dimension, BiConsumer<@Nonnull ? super Double, @Nonnull ? super Double> action) {
		action.accept(dimension.getWidth(), dimension.getHeight());
	}

	@SuppressWarnings("AutoUnboxing")
	public static <R extends RectangularShape> R floorRectangularShape(RectangularShape source, R destination) {
		acceptRectangularShape(source, (x, y, w, h) ->
				destination.setFrame(Math.floor(x), Math.floor(y), Math.floor(w), Math.floor(h)));
		return destination;
	}

	@SuppressWarnings("AutoBoxing")
	public static <TH extends Throwable> void acceptRectangularShape(RectangularShape rectangular, IConsumer4<@Nonnull ? super Double, @Nonnull ? super Double, @Nonnull ? super Double, @Nonnull ? super Double, ? extends TH> action)
			throws TH {
		action.accept(rectangular.getX(), rectangular.getY(), rectangular.getWidth(), rectangular.getHeight());
	}

	@SuppressWarnings("AutoUnboxing")
	public static <R extends Point2D> R floorPoint(Point2D source, R destination) {
		acceptPoint(source, (x, y) -> destination.setLocation(Math.floor(x), Math.floor(y)));
		return destination;
	}

	@SuppressWarnings("AutoBoxing")
	public static void acceptPoint(Point2D point, BiConsumer<@Nonnull ? super Double, @Nonnull ? super Double> action) {
		action.accept(point.getX(), point.getY());
	}

	@SuppressWarnings("AutoUnboxing")
	public static <R extends Point2D> R minPoint(Point2D a, Point2D b, R destination) {
		acceptPoint(a, (ax, ay) -> acceptPoint(b, (bx, by) -> destination.setLocation(min(ax, bx), min(ay, by))));
		return destination;
	}

	@SuppressWarnings("AutoUnboxing")
	public static <R extends Point2D> R maxPoint(Point2D a, Point2D b, R destination) {
		acceptPoint(a, (ax, ay) -> acceptPoint(b, (bx, by) -> destination.setLocation(max(ax, bx), max(ay, by))));
		return destination;
	}

	@SuppressWarnings("AutoBoxing")
	public static <T> T applyPoint(Point2D point, BiFunction<@Nonnull ? super Double, @Nonnull ? super Double, ? extends T> action) {
		return action.apply(point.getX(), point.getY());
	}

	@SuppressWarnings("AutoBoxing")
	public static <T> T applyDimension(Dimension2D dimension, BiFunction<@Nonnull ? super Double, @Nonnull ? super Double, ? extends T> action) {
		return action.apply(dimension.getWidth(), dimension.getHeight());
	}

	@SuppressWarnings({"UnusedReturnValue", "AutoUnboxing"})
	public static <R extends RectangularShape> R transformRectangularShape(AffineTransform transform,
	                                                                       RectangularShape rectangular,
	                                                                       R destination) {
		UIObjectUtilities.acceptRectangularShape(rectangular, (x, y, w, h) ->
				destination.setFrame(x * transform.getScaleX() + transform.getTranslateX(), y * transform.getScaleY() + transform.getTranslateY(),
						w * transform.getScaleX(), h * transform.getScaleY())
		);
		return destination;
	}

	@SuppressWarnings("AutoBoxing")
	public static <T, TH extends Throwable> T applyRectangularShape(RectangularShape rectangular, IFunction4<? super Double, ? super Double, ? super Double, ? super Double, ? extends T, ? extends TH> action)
			throws TH {
		return action.apply(rectangular.getX(), rectangular.getY(), rectangular.getWidth(), rectangular.getHeight());
	}
}
