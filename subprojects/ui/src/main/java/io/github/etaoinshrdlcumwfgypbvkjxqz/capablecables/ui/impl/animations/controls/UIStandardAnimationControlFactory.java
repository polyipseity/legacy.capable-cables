package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.animations.controls;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.core.IFunction3;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.time.core.ITicker;

public enum UIStandardAnimationControlFactory {
	;

	public static final int INFINITE_LOOP = -1;

	public static UIAbstractStandardAnimationControl createSimple(EnumDirection direction,
	                                                              IUIAnimationTarget target,
	                                                              ITicker ticker,
	                                                              boolean autoPlay,
	                                                              long duration,
	                                                              long startDelay,
	                                                              long endDelay,
	                                                              long loop) {
		switch (direction) {
			case NORMAL:
				return new UIMonoDirectionalSimpleStandardAnimationControl(target,
						ticker,
						autoPlay,
						duration,
						startDelay,
						endDelay,
						loop);
			case REVERSE:
				return new UIReversedMonoDirectionalSimpleStandardAnimationControl(target,
						ticker,
						autoPlay,
						duration,
						startDelay,
						endDelay,
						loop);
			case ALTERNATE:
				return new UIBiDirectionalSimpleStandardAnimationControl(target,
						ticker,
						autoPlay,
						duration,
						startDelay,
						endDelay,
						loop);
			default:
				throw new AssertionError();
		}
	}

	public static UIAbstractStandardAnimationControl create(EnumDirection direction,
	                                                        Iterable<? extends IUIAnimationTarget> targets,
	                                                        ITicker ticker,
	                                                        boolean autoPlay,
	                                                        IFunction3<? super IUIAnimationTarget, ? super Integer, ? super Integer, ? extends Long, ? extends RuntimeException> durationFunction,
	                                                        IFunction3<? super IUIAnimationTarget, ? super Integer, ? super Integer, ? extends Long, ? extends RuntimeException> startDelayFunction,
	                                                        IFunction3<? super IUIAnimationTarget, ? super Integer, ? super Integer, ? extends Long, ? extends RuntimeException> endDelayFunction,
	                                                        IFunction3<? super IUIAnimationTarget, ? super Integer, ? super Integer, ? extends Long, ? extends RuntimeException> loopFunction) {
		switch (direction) {
			case NORMAL:
				return new UIMonoDirectionalStandardAnimationControl(targets,
						ticker,
						autoPlay,
						durationFunction,
						startDelayFunction,
						endDelayFunction,
						loopFunction);
			case REVERSE:
				return new UIReversedMonoDirectionalStandardAnimationControl(targets,
						ticker,
						autoPlay,
						durationFunction,
						startDelayFunction,
						endDelayFunction,
						loopFunction);
			case ALTERNATE:
				return new UIBiDirectionalStandardAnimationControl(targets,
						ticker,
						autoPlay,
						durationFunction,
						startDelayFunction,
						endDelayFunction,
						loopFunction);
			default:
				throw new AssertionError();
		}
	}

	public static int getInfiniteLoop() { return INFINITE_LOOP; }

	public enum EnumDirection {
		NORMAL,
		REVERSE,
		ALTERNATE,
	}
}
