package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.animations.easings;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.MathUtilities;

import java.awt.geom.Point2D;

@Immutable
public final class CubicBezierCurveUIAnimationEasing
		extends AbstractBezierCurveUIAnimationEasing {
	private CubicBezierCurveUIAnimationEasing(Point2D controlPoint1, Point2D controlPoint2) {
		super(ImmutableList.of(getControlPointStart(), controlPoint1, controlPoint2, getControlPointEnd()));
	}

	public static CubicBezierCurveUIAnimationEasing of(Point2D controlPoint1, Point2D controlPoint2) {
		return new CubicBezierCurveUIAnimationEasing((Point2D) controlPoint1.clone(), (Point2D) controlPoint2.clone());
	}

	@Override
	protected double getTForX(double x) {
		return MathUtilities.BezierCurves.getCubicTForX(x, getControlPoints())
				.orElseThrow(IllegalArgumentException::new);
	}
}
