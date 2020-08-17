package $group__.client.ui.structures;

import javax.annotation.concurrent.Immutable;
import java.awt.geom.Point2D;

@Immutable
public class Point2DImmutable
		extends Point2D {
	protected final double x, y;

	public Point2DImmutable(Point2D point) {
		this(point.getX(), point.getY());
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
	public void setLocation(double x, double y) { throw new UnsupportedOperationException(); }

	@Override
	public Point2DImmutable clone() {
		return (Point2DImmutable) super.clone();
	}
}
