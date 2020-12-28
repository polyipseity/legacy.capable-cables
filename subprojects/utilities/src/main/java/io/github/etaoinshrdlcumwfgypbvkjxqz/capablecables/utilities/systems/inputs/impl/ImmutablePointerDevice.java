package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AbstractDelegatingObject;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.ICursor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.IPointerDevice;

import java.awt.geom.Point2D;

public final class ImmutablePointerDevice
		extends AbstractDelegatingObject<IPointerDevice>
		implements IPointerDevice {
	private final Point2D position;

	private ImmutablePointerDevice(IPointerDevice delegate) {
		super(delegate);
		this.position = delegate.getPositionView();
	}

	public static ImmutablePointerDevice of(IPointerDevice delegate) {
		return new ImmutablePointerDevice(delegate);
	}

	@Override
	public Point2D getPositionView() {
		return (Point2D) getPosition().clone();
	}

	@Override
	public void setCursor(ICursor cursor) {
		getDelegate().setCursor(cursor);
	}

	protected Point2D getPosition() {
		return position;
	}
}
