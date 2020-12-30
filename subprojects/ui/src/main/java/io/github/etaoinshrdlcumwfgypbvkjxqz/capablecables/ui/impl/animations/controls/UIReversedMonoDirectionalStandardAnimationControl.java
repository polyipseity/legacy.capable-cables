package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.animations.controls;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.animations.IUIAnimationTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.animations.IUIAnimationTime;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.MathUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.def.IFunction3;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.time.def.ITicker;

public class UIReversedMonoDirectionalStandardAnimationControl
		extends UIAbstractStandardAnimationControl {
	protected UIReversedMonoDirectionalStandardAnimationControl(Iterable<? extends IUIAnimationTarget> targets,
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
		long loop = getLoops().getLong(index);
		if (loop != UIStandardAnimationControlFactory.getInfiniteLoop()) {
			double currentLoop = getCurrentLoop(this, index);
			if (currentLoop >= loop)
				currentProgress = 0;
			else if (currentLoop < 0)
				currentProgress = 1;
		}
		return (double) currentProgress / getLocalDurations().getLong(index);
	}

	protected static long getCurrentProgress(UIReversedMonoDirectionalStandardAnimationControl instance, int index) {
		return MathUtilities.clamp(
				Math.floorMod(instance.getElapsed(), getTotalDuration(instance, index)) - instance.getEndDelays().getLong(index),
				0,
				instance.getLocalDurations().getLong(index));
	}

	protected static double getCurrentLoop(UIReversedMonoDirectionalStandardAnimationControl instance, int index) {
		return MathUtilities.roundToZero((double) instance.getElapsed() / getTotalDuration(instance, index));
	}

	protected static long getTotalDuration(UIReversedMonoDirectionalStandardAnimationControl instance, int index) {
		return instance.getEndDelays().getLong(index)
				+ instance.getLocalDurations().getLong(index)
				+ instance.getStartDelays().getLong(index);
	}

	@Override
	protected IUIAnimationTime calculateTotalDuration() {
		return calculateTotalDuration(this, UIReversedMonoDirectionalStandardAnimationControl::getTotalDuration);
	}
}
