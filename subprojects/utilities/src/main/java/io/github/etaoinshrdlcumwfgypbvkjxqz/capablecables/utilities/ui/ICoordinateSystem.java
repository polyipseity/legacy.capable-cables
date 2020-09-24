package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ui;

import java.awt.geom.Point2D;

public interface ICoordinateSystem {
	Point2D specialize(Point2D point);

	Point2D generalize(Point2D point);
}
