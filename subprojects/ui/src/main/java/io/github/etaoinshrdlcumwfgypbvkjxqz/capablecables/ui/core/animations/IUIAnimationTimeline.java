package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations;

import java.util.function.LongUnaryOperator;

public interface IUIAnimationTimeline
		extends IUIAnimationControllable {
	boolean append(IUIAnimationControl control)
			throws AlreadyInfiniteUIAnimationTimelineException;

	boolean append(IUIAnimationControl control, long offset)
			throws AlreadyInfiniteUIAnimationTimelineException;

	boolean append(IUIAnimationControl control, LongUnaryOperator offsetFunction)
			throws AlreadyInfiniteUIAnimationTimelineException;

	void play();
}
