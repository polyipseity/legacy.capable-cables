package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.animations.controls;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IFunction3;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.time.ITicker;

public enum UIStandardAnimationControlFactory {
	;

	public static final int INFINITE_LOOP = -1;

	public static AbstractUIStandardAnimationControl createSimple(EnumDirection direction,
	                                                              IUIAnimationTarget target,
	                                                              ITicker ticker,
	                                                              boolean autoPlay,
	                                                              long duration,
	                                                              long startDelay,
	                                                              long endDelay,
	                                                              long loop) {
		switch (direction) {
			case NORMAL:
				return new MonoDirectionalSimpleUIStandardAnimationControl(target,
						ticker,
						autoPlay,
						duration,
						startDelay,
						endDelay,
						loop);
			case REVERSE:
				return new ReversedMonoDirectionalSimpleUIStandardAnimationControl(target,
						ticker,
						autoPlay,
						duration,
						startDelay,
						endDelay,
						loop);
			case ALTERNATE:
				return new BiDirectionalSimpleUIStandardAnimationControl(target,
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

	public static AbstractUIStandardAnimationControl create(EnumDirection direction,
	                                                        Iterable<? extends IUIAnimationTarget> targets,
	                                                        ITicker ticker,
	                                                        boolean autoPlay,
	                                                        IFunction3<? super IUIAnimationTarget, ? super Integer, ? super Integer, ? extends Long, ? extends RuntimeException> durationFunction,
	                                                        IFunction3<? super IUIAnimationTarget, ? super Integer, ? super Integer, ? extends Long, ? extends RuntimeException> startDelayFunction,
	                                                        IFunction3<? super IUIAnimationTarget, ? super Integer, ? super Integer, ? extends Long, ? extends RuntimeException> endDelayFunction,
	                                                        IFunction3<? super IUIAnimationTarget, ? super Integer, ? super Integer, ? extends Long, ? extends RuntimeException> loopFunction) {
		switch (direction) {
			case NORMAL:
				return new MonoDirectionalUIStandardAnimationControl(targets,
						ticker,
						autoPlay,
						durationFunction,
						startDelayFunction,
						endDelayFunction,
						loopFunction);
			case REVERSE:
				return new ReversedMonoDirectionalUIStandardAnimationControl(targets,
						ticker,
						autoPlay,
						durationFunction,
						startDelayFunction,
						endDelayFunction,
						loopFunction);
			case ALTERNATE:
				return new BiDirectionalUIStandardAnimationControl(targets,
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
