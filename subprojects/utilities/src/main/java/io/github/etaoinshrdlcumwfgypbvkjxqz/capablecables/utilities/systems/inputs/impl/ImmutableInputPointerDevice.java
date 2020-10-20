package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.IInputPointerDevice;

import javax.annotation.concurrent.Immutable;
import java.awt.geom.Point2D;

@Immutable
public final class ImmutableInputPointerDevice
		implements IInputPointerDevice {
	private final Point2D position;

	private ImmutableInputPointerDevice(Point2D position) {
		this.position = (Point2D) position.clone();
	}

	public static ImmutableInputPointerDevice of(IInputPointerDevice source) { return of(source.getPositionView()); }

	public static ImmutableInputPointerDevice of(Point2D position) {
		return new ImmutableInputPointerDevice(position);
	}

	@Override
	public Point2D getPositionView() {
		return (Point2D) getPosition().clone();
	}

	protected Point2D getPosition() {
		return position;
	}
}
