package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.animations.easings;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.MathUtilities;

import java.awt.geom.Point2D;

@Immutable
public final class UIQuadraticBezierCurveAnimationEasing
		extends AbstractBezierCurveUIAnimationEasing {
	private UIQuadraticBezierCurveAnimationEasing(Point2D controlPoint1) {
		super(ImmutableList.of(getControlPointStart(), (Point2D) controlPoint1.clone(), getControlPointEnd()));
	}

	public static UIQuadraticBezierCurveAnimationEasing of(Point2D controlPoint1) {
		return new UIQuadraticBezierCurveAnimationEasing(controlPoint1);
	}

	@Override
	protected double getTForX(double x) {
		return MathUtilities.BezierCurves.getQuadraticTForX(x, getControlPoints().iterator())
				.orElseThrow(IllegalArgumentException::new);
	}
}
