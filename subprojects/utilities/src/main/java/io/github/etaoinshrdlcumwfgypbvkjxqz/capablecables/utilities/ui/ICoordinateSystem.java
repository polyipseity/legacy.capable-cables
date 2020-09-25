package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ui;

import java.awt.geom.Point2D;

public interface ICoordinateSystem {
	<R extends Point2D> R specialize(Point2D source, R destination);

	<R extends Point2D> R generalize(Point2D source, R destination);
}
