package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.animations.easings;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationEasing;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.MathUtilities;

import java.awt.geom.Point2D;
import java.util.List;

public abstract class AbstractBezierCurveUIAnimationEasing
		implements IUIAnimationEasing {
	private static final Point2D CONTROL_POINT_START = new Point2D.Double(0, 0);
	private static final Point2D CONTROL_POINT_END = new Point2D.Double(1, 1);
	private final List<Point2D> controlPoints;

	protected AbstractBezierCurveUIAnimationEasing(Iterable<? extends Point2D> controlPoints) {
		this.controlPoints = ImmutableList.copyOf(controlPoints);
	}

	public static Point2D getControlPointStart() { return (Point2D) CONTROL_POINT_START.clone(); }

	public static Point2D getControlPointEnd() { return (Point2D) CONTROL_POINT_END.clone(); }

	@Override
	public double applyAsDouble(double x) {
		return MathUtilities.BezierCurves.bezierCurveExact(new Point2D.Double(), getTForX(x), getControlPoints()).getY();
	}

	protected abstract double getTForX(double x);

	protected List<Point2D> getControlPoints() { return controlPoints; }
}
