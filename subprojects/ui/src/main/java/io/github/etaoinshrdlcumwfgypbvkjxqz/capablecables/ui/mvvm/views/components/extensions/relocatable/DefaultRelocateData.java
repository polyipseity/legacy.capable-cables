package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.extensions.relocatable;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.IUIComponentUserRelocatableExtension;

import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;

public class DefaultRelocateData implements IUIComponentUserRelocatableExtension.IRelocateData {
	protected final Point2D previousPoint;

	public DefaultRelocateData(Point2D previousPoint) {
		this.previousPoint = (Point2D) previousPoint.clone();
	}

	@Override
	public Point2D getPreviousPointView() { return (Point2D) getPreviousPoint().clone(); }

	protected Point2D getPreviousPoint() { return previousPoint; }

	@Override
	public <R extends RectangularShape> R handle(Point2D point, RectangularShape source, R destination) {
		Point2D previousPoint = getPreviousPoint();
		destination.setFrame(
				source.getX() + (point.getX() - previousPoint.getX()),
				source.getY() + (point.getY() - previousPoint.getY()),
				source.getWidth(),
				source.getHeight());
		return destination;
	}
}
