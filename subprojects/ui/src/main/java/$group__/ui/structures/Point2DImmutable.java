package $group__.ui.structures;

import javax.annotation.concurrent.Immutable;
import java.awt.geom.Point2D;

@Immutable
public class Point2DImmutable
		extends Point2D {
	protected final double x, y;

	protected Point2DImmutable(Point2D point) {
		this(point.getX(), point.getY());
	}

	public static Point2DImmutable copyOf(Point2D point) {
		if (point instanceof Point2DImmutable)
			return (Point2DImmutable) point;
		return new Point2DImmutable(point);
	}

	public Point2DImmutable(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Point2DImmutable() { this(0, 0); }

	@Override
	public double getX() { return x; }

	@Override
	public double getY() { return y; }

	@Override
	public void setLocation(double x, double y)
			throws UnsupportedOperationException { throw new UnsupportedOperationException(); }

	@Override
	public Point2DImmutable clone() {
		return (Point2DImmutable) super.clone();
	}
}
