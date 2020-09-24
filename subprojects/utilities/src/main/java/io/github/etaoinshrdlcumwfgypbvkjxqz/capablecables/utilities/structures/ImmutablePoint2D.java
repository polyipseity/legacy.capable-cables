package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ui.UIObjectUtilities;

import javax.annotation.concurrent.Immutable;
import java.awt.geom.Point2D;

@Immutable
public final class ImmutablePoint2D
		extends Point2D {
	private static final ImmutablePoint2D DEFAULT = new ImmutablePoint2D(0, 0);
	private final double x;
	private final double y;

	private ImmutablePoint2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public static ImmutablePoint2D of(Point2D point) {
		if (point instanceof ImmutablePoint2D)
			return (ImmutablePoint2D) point;
		return UIObjectUtilities.applyPoint(point, ImmutablePoint2D::of);
	}

	public static ImmutablePoint2D of(double x, double y) { return new ImmutablePoint2D(x, y); }

	public static ImmutablePoint2D of() { return DEFAULT; }

	@Override
	public double getX() { return x; }

	@Override
	public double getY() { return y; }

	@Override
	@Deprecated
	public void setLocation(double x, double y)
			throws UnsupportedOperationException { throw new UnsupportedOperationException(); }

	@Override
	public ImmutablePoint2D clone() {
		return (ImmutablePoint2D) super.clone();
	}
}
