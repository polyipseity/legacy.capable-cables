package $group__.client.ui.utilities;

import $group__.client.ui.mvvm.structures.Dimension2DDouble;
import $group__.utilities.client.minecraft.TransformUtilities.AffineTransformUtilities;

import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static java.lang.Math.max;
import static java.lang.Math.min;

public enum UIObjectUtilities {
	;

	public static Shape copyShape(Shape shape) { return AffineTransformUtilities.getIdentity().createTransformedShape(shape); }

	public static Rectangle2D getRectangleFromDiagonal(Point2D p1, Point2D p2) {
		Rectangle2D ret = new Rectangle2D.Double();
		ret.setFrameFromDiagonal(p1, p2);
		return ret;
	}

	public static Dimension getDimensionFloor(Dimension2D dimension) { return new Dimension((int) Math.floor(dimension.getWidth()), (int) Math.floor(dimension.getHeight())); }

	public static Rectangle getRectangleExpanded(Rectangle2D rectangle) {
		Rectangle ret = new Rectangle();
		ret.setFrameFromDiagonal(getPointFloor(new Point2D.Double(rectangle.getX(), rectangle.getY())), getPointCeil(new Point2D.Double(rectangle.getMaxX(), rectangle.getMaxY())));
		return ret;
	}

	public static Point getPointFloor(Point2D point) { return new Point((int) Math.floor(point.getX()), (int) Math.floor(point.getY())); }

	public static Point getPointCeil(Point2D point) { return new Point((int) Math.ceil(point.getX()), (int) Math.ceil(point.getY())); }

	public static <T> void acceptPoint(Point2D point, BiConsumer<Double, Double> user) { user.accept(point.getX(), point.getY()); }

	public static <T> void acceptDimension(Dimension2D dimension, BiConsumer<Double, Double> user) { user.accept(dimension.getWidth(), dimension.getHeight()); }

	public static void acceptRectangular(RectangularShape rectangular, BiFunction<Double, Double, BiConsumer<Double, Double>> user) { user.apply(rectangular.getX(), rectangular.getY()).accept(rectangular.getWidth(), rectangular.getHeight()); }

	public static Point2D minPoint(Point2D a, Point2D b) { return applyPoint(a, (ax, ay) -> applyPoint(b, (bx, by) -> new Point2D.Double(min(ax, bx), min(ay, by)))); }

	public static <T> T applyPoint(Point2D point, BiFunction<Double, Double, T> user) { return user.apply(point.getX(), point.getY()); }

	public static Point2D maxPoint(Point2D a, Point2D b) { return applyPoint(a, (ax, ay) -> applyPoint(b, (bx, by) -> new Point2D.Double(max(ax, bx), max(ay, by)))); }

	public static Dimension2D minDimension(Dimension2D a, Dimension2D b) { return applyDimension(a, (ax, ay) -> applyDimension(b, (bx, by) -> new Dimension2DDouble(min(ax, bx), min(ay, by)))); }

	public static <T> T applyDimension(Dimension2D dimension, BiFunction<Double, Double, T> user) { return user.apply(dimension.getWidth(), dimension.getHeight()); }

	public static Dimension2D maxDimension(Dimension2D a, Dimension2D b) { return applyDimension(a, (ax, ay) -> applyDimension(b, (bx, by) -> new Dimension2DDouble(max(ax, bx), max(ay, by)))); }

	public static Rectangle2D minRectangle(Rectangle2D a, Rectangle2D b) { return applyRectangular(a, (ax, ay) -> (aw, ah) -> applyRectangular(b, (bx, by) -> (bw, bh) -> new Rectangle2D.Double(min(ax, bx), min(ay, by), min(aw, bw), min(ah, bh)))); }

	public static <T> T applyRectangular(RectangularShape rectangular, BiFunction<Double, Double, BiFunction<Double, Double, T>> user) { return user.apply(rectangular.getX(), rectangular.getY()).apply(rectangular.getWidth(), rectangular.getHeight()); }

	public static Rectangle2D maxRectangle(Rectangle2D a, Rectangle2D b) { return applyRectangular(a, (ax, ay) -> (aw, ah) -> applyRectangular(b, (bx, by) -> (bw, bh) -> new Rectangle2D.Double(max(ax, bx), max(ay, by), max(aw, bw), max(ah, bh)))); }
}
