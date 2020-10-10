package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations;

import java.util.function.LongUnaryOperator;

public interface IUIAnimationTimeline
		extends IUIAnimationControllable {
	boolean append(IUIAnimationControl control);

	boolean append(IUIAnimationControl control, long offset);

	boolean append(IUIAnimationControl control, LongUnaryOperator offsetFunction);

	void play();
}
