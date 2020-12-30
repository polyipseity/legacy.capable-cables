package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.animations.easings;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.MathUtilities;

import java.awt.geom.Point2D;

@Immutable
public final class UICubicBezierCurveAnimationEasing
		extends AbstractBezierCurveUIAnimationEasing {
	private UICubicBezierCurveAnimationEasing(Point2D controlPoint1, Point2D controlPoint2) {
		super(ImmutableList.of(getControlPointStart(), (Point2D) controlPoint1.clone(), (Point2D) controlPoint2.clone(), getControlPointEnd()));
	}

	public static UICubicBezierCurveAnimationEasing of(Point2D controlPoint1, Point2D controlPoint2) {
		return new UICubicBezierCurveAnimationEasing(controlPoint1, controlPoint2);
	}

	@Override
	protected double getTForX(double x) {
		return MathUtilities.BezierCurves.getCubicTForX(x, getControlPoints().iterator())
				.orElseThrow(IllegalArgumentException::new);
	}
}
