package $group__.ui.structures;

import java.awt.geom.Point2D;

public enum EnumUIAxis {
	X {
		@Override
		public double getCoordinate(Point2D point) { return point.getX(); }
	},
	Y {
		@Override
		public double getCoordinate(Point2D point) { return point.getY(); }
	};

	public abstract double getCoordinate(Point2D point);
}
