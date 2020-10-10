package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.animations.easings;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.MathUtilities;

import java.awt.geom.Point2D;

@Immutable
public final class QuadraticBezierCurveUIAnimationEasing
		extends AbstractBezierCurveUIAnimationEasing {
	private QuadraticBezierCurveUIAnimationEasing(Point2D controlPoint1) {
		super(ImmutableList.of(getControlPointStart(), controlPoint1, getControlPointEnd()));
	}

	public static QuadraticBezierCurveUIAnimationEasing of(Point2D controlPoint1) {
		return new QuadraticBezierCurveUIAnimationEasing((Point2D) controlPoint1.clone());
	}

	@Override
	protected double getTForX(double x) {
		return MathUtilities.BezierCurves.getQuadraticTForX(x, getControlPoints())
				.orElseThrow(IllegalArgumentException::new);
	}
}
