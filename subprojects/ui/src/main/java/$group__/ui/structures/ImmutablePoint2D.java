package $group__.ui.structures;

import javax.annotation.concurrent.Immutable;
import java.awt.geom.Point2D;

@Immutable
public class ImmutablePoint2D
		extends Point2D {
	protected final double x, y;

	protected ImmutablePoint2D(Point2D point) {
		this(point.getX(), point.getY());
	}

	public ImmutablePoint2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public ImmutablePoint2D() { this(0, 0); }

	public static ImmutablePoint2D copyOf(Point2D point) {
		if (point instanceof ImmutablePoint2D)
			return (ImmutablePoint2D) point;
		return new ImmutablePoint2D(point);
	}

	@Override
	public double getX() { return x; }

	@Override
	public double getY() { return y; }

	@Override
	public void setLocation(double x, double y)
			throws UnsupportedOperationException { throw new UnsupportedOperationException(); }

	@Override
	public ImmutablePoint2D clone() {
		return (ImmutablePoint2D) super.clone();
	}
}
