package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.animations.controls;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationTime;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.MathUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IFunction3;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.time.core.ITicker;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities.assertNonnull;

public class UIMonoDirectionalStandardAnimationControl
		extends UIAbstractStandardAnimationControl {
	protected UIMonoDirectionalStandardAnimationControl(Iterable<? extends IUIAnimationTarget> targets,
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

	protected static long getCurrentProgress(UIMonoDirectionalStandardAnimationControl instance, int index) {
		return MathUtilities.clamp(
				Math.floorMod(instance.getElapsed(), getTotalDuration(instance, index)) - assertNonnull(instance.getStartDelays().get(index)),
				0,
				assertNonnull(instance.getLocalDurations().get(index)));
	}

	protected static double getCurrentLoop(UIMonoDirectionalStandardAnimationControl instance, int index) {
		return MathUtilities.roundToZero((double) instance.getElapsed() / getTotalDuration(instance, index));
	}

	protected static long getTotalDuration(UIMonoDirectionalStandardAnimationControl instance, int index) {
		return assertNonnull(instance.getStartDelays().get(index))
				+ assertNonnull(instance.getLocalDurations().get(index))
				+ assertNonnull(instance.getEndDelays().get(index));
	}

	@Override
	protected IUIAnimationTime calculateTotalDuration() {
		return calculateTotalDuration(this, UIMonoDirectionalStandardAnimationControl::getTotalDuration);
	}
}
