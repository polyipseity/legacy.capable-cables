package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.extensions.relocatable;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.IUIComponentUserRelocatableExtension;

import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;

public class DefaultRelocateData implements IUIComponentUserRelocatableExtension.IRelocateData {
	protected final Point2D point;

	public DefaultRelocateData(Point2D point) {
		this.point = (Point2D) point.clone();
	}

	@Override
	public Point2D getPointView() { return (Point2D) getPoint().clone(); }

	protected Point2D getPoint() { return point; }

	@Override
	public <R extends RectangularShape> R handle(Point2D point, RectangularShape source, R destination) {
		Point2D previousCursorPosition = getPoint();
		destination.setFrame(
				source.getX() + (point.getX() - previousCursorPosition.getX()),
				source.getY() + (point.getY() - previousCursorPosition.getY()),
				source.getWidth(),
				source.getHeight());
		return destination;
	}
}
