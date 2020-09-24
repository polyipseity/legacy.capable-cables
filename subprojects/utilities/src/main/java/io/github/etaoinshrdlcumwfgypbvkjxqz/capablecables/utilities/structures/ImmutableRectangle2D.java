package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ui.UIObjectUtilities;

import javax.annotation.concurrent.Immutable;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

@Immutable
public final class ImmutableRectangle2D
		extends Rectangle2D {
	private static final ImmutableRectangle2D DEFAULT = new ImmutableRectangle2D(0, 0, 0, 0);
	private final double x;
	private final double y;
	private final double width;
	private final double height;

	private ImmutableRectangle2D(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public static ImmutableRectangle2D of() { return DEFAULT; }

	public static ImmutableRectangle2D of(Point2D position, Dimension2D dimension) {
		return of(position.getX(), position.getY(), dimension.getWidth(), dimension.getHeight());
	}

	public static ImmutableRectangle2D of(double x, double y, double width, double height) {
		return new ImmutableRectangle2D(x, y, width, height);
	}

	public static ImmutableRectangle2D fromDiagonal(Point2D point1, Point2D point2) {
		Rectangle2D ret = new Rectangle2D.Double();
		ret.setFrameFromDiagonal(point1, point2);
		return of(ret);
	}

	public static ImmutableRectangle2D of(Rectangle2D rectangle) {
		if (rectangle instanceof ImmutableRectangle2D)
			return (ImmutableRectangle2D) rectangle;
		return UIObjectUtilities.applyRectangular(rectangle, ImmutableRectangle2D::of);
	}

	@Override
	@Deprecated
	public void setRect(double x, double y, double w, double h)
			throws UnsupportedOperationException { throw new UnsupportedOperationException(); }

	@Override
	public int outcode(double x, double y) {
		int out = 0;
		if (getWidth() <= 0) {
			out |= OUT_LEFT | OUT_RIGHT;
		} else if (x < getX()) {
			out |= OUT_LEFT;
		} else if (x > getX() + getWidth()) {
			out |= OUT_RIGHT;
		}
		if (getHeight() <= 0) {
			out |= OUT_TOP | OUT_BOTTOM;
		} else if (y < getY()) {
			out |= OUT_TOP;
		} else if (y > getY() + getHeight()) {
			out |= OUT_BOTTOM;
		}
		return out;
	}

	@Override
	public ImmutableRectangle2D createIntersection(Rectangle2D r) {
		Rectangle2D rect = new Rectangle2D.Double();
		Rectangle2D.intersect(this, r, rect);
		return ImmutableRectangle2D.of(rect);
	}

	@Override
	public ImmutableRectangle2D createUnion(Rectangle2D r) {
		Rectangle2D rect = new Rectangle2D.Double();
		Rectangle2D.union(this, r, rect);
		return ImmutableRectangle2D.of(rect);
	}

	@Override
	public double getX() { return x; }

	@Override
	public double getY() { return y; }

	@Override
	public double getWidth() { return width; }

	@Override
	public double getHeight() { return height; }

	@Override
	public boolean isEmpty() { return (getWidth() <= 0.0) || (getHeight() <= 0.0); }

	@Override
	public ImmutableRectangle2D clone() { return (ImmutableRectangle2D) super.clone(); }
}
