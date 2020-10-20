package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.animations.controls;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationTime;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.MathUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IFunction3;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.time.core.ITicker;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities.assertNonnull;

public class UIBiDirectionalStandardAnimationControl
		extends UIAbstractStandardAnimationControl {
	protected UIBiDirectionalStandardAnimationControl(Iterable<? extends IUIAnimationTarget> targets,
	                                                  ITicker ticker,
	                                                  boolean autoPlay,
	                                                  IFunction3<? super IUIAnimationTarget, ? super Integer, ? super Integer, ? extends Long, ? extends RuntimeException> durationFunction,
	                                                  IFunction3<? super IUIAnimationTarget, ? super Integer, ? super Integer, ? extends Long, ? extends RuntimeException> startDelayFunction,
	                                                  IFunction3<? super IUIAnimationTarget, ? super Integer, ? super Integer, ? extends Long, ? extends RuntimeException> endDelayFunction,
	                                                  IFunction3<? super IUIAnimationTarget, ? super Integer, ? super Integer, ? extends Long, ? extends RuntimeException> loopFunction) {
		super(targets, ticker, autoPlay, durationFunction, startDelayFunction, endDelayFunction, loopFunction);
	}

	@Override
	protected double getProgressForTarget(IUIAnimationTarget target, int index, int size) {
		long currentProgress = getCurrentProgress(this, index);
		long loop = assertNonnull(getLoops().get(index));
		if (loop != UIStandardAnimationControlFactory.getInfiniteLoop()) {
			double currentLoop = getCurrentLoop(this, index);
			if (currentLoop >= loop)
				currentProgress = 1;
			else if (currentLoop < 0)
				currentProgress = 0;
		}
		return (double) currentProgress / assertNonnull(getLocalDurations().get(index));
	}

	protected static long getCurrentProgress(UIBiDirectionalStandardAnimationControl instance, int index) {
		long totalDuration = getTotalDuration(instance, index);
		long progress = Math.floorMod(instance.getElapsed(), totalDuration); // COMMENT function shape is /
		long roundedProgress = Math.round((double) progress / totalDuration) * totalDuration;
		long actualProgress = Math.abs(progress - roundedProgress); // COMMENT function shape is /\
		return MathUtilities.clamp(actualProgress - assertNonnull(instance.getStartDelays().get(index)),
				0,
				assertNonnull(instance.getLocalDurations().get(index)));
	}

	protected static double getCurrentLoop(UIBiDirectionalStandardAnimationControl instance, int index) {
		return MathUtilities.roundToZero((double) instance.getElapsed() / getTotalDuration(instance, index));
	}

	protected static long getTotalDuration(UIBiDirectionalStandardAnimationControl instance, int index) {
		return (assertNonnull(instance.getStartDelays().get(index))
				+ assertNonnull(instance.getLocalDurations().get(index))
				+ assertNonnull(instance.getEndDelays().get(index))) << 1;
	}

	@Override
	protected IUIAnimationTime calculateTotalDuration() {
		return calculateTotalDuration(this, UIBiDirectionalStandardAnimationControl::getTotalDuration);
	}
}
