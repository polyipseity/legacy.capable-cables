package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.animations;

import java.util.function.LongUnaryOperator;

public interface IUIAnimationTimeline
		extends IUIAnimationControllable {
	boolean append(IUIAnimationControl control)
			throws UIAlreadyInfiniteAnimationTimelineException;

	boolean append(IUIAnimationControl control, long offset)
			throws UIAlreadyInfiniteAnimationTimelineException;

	boolean append(IUIAnimationControl control, LongUnaryOperator offsetFunction)
			throws UIAlreadyInfiniteAnimationTimelineException;

	void play();
}
