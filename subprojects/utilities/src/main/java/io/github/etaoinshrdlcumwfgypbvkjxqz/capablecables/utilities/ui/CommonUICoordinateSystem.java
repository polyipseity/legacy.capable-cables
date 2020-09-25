package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ui;

import java.awt.geom.Point2D;

public enum CommonUICoordinateSystem
		implements ICoordinateSystem {
	INSTANCE,
	;

	@Override
	public <R extends Point2D> R specialize(Point2D source, R destination) {
		destination.setLocation(source);
		return destination;
	}

	@Override
	public <R extends Point2D> R generalize(Point2D source, R destination) {
		destination.setLocation(source);
		return destination;
	}
}
