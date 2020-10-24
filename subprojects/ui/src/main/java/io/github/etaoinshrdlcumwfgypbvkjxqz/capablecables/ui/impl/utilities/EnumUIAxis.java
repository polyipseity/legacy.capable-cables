package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities;

import javax.annotation.concurrent.Immutable;
import java.awt.geom.Point2D;

@Immutable
public enum EnumUIAxis {
	X {
		@Override
		public double getCoordinate(Point2D point) { return point.getX(); }

		@Override
		public void setCoordinate(Point2D point, double value) {
			point.setLocation(value, point.getY());
		}
	},
	Y {
		@Override
		public double getCoordinate(Point2D point) { return point.getY(); }

		@Override
		public void setCoordinate(Point2D point, double value) {
			point.setLocation(point.getX(), value);
		}
	};

	public abstract double getCoordinate(Point2D point);

	public abstract void setCoordinate(Point2D point, double value);
}
