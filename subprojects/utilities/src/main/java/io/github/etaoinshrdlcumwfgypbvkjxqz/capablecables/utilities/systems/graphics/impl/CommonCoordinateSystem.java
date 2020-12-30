package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.graphics.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.graphics.def.ICoordinateSystem;

import java.awt.geom.Point2D;

public enum CommonCoordinateSystem
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
