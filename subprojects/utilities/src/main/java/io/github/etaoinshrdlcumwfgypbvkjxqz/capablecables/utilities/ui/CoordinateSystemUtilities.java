package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ui;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.ImmutablePoint2D;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.ImmutableRectangle2D;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class CoordinateSystemUtilities {
	;

	public static ImmutableRectangle2D convertRectangle(Rectangle2D rectangle, ICoordinateSystem from, ICoordinateSystem to) {
		return ImmutableRectangle2D.fromDiagonal(
				convertPoint(ImmutablePoint2D.of(rectangle.getX(), rectangle.getY()), from, to),
				convertPoint(ImmutablePoint2D.of(rectangle.getMaxX(), rectangle.getMaxY()), from, to));
	}

	public static ImmutablePoint2D convertPoint(Point2D point, ICoordinateSystem from, ICoordinateSystem to) {
		return ImmutablePoint2D.of(to.specialize(from.generalize(point)));
	}
}
