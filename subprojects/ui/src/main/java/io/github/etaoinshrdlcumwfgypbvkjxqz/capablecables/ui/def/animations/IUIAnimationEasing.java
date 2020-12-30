package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.animations;

import java.util.function.DoubleUnaryOperator;

@FunctionalInterface
public interface IUIAnimationEasing
		extends DoubleUnaryOperator {
	@Override
	double applyAsDouble(double x);
}
