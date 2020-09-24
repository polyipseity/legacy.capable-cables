package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ui;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.ImmutablePoint2D;

import java.awt.geom.Point2D;

public enum CommonUICoordinateSystem
		implements ICoordinateSystem {
	INSTANCE,
	;

	@Override
	public Point2D specialize(Point2D point) { return ImmutablePoint2D.of(point); }

	@Override
	public Point2D generalize(Point2D point) { return ImmutablePoint2D.of(point); }
}
