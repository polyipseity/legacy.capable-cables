package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.animations.timelines;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationControl;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationTimeline;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.UIAlreadyInfiniteAnimationTimelineException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.animations.UIAbstractAnimationPlayable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.time.core.ITicker;

import java.util.function.LongUnaryOperator;

public abstract class UIAbstractAnimationTimeline
		extends UIAbstractAnimationPlayable
		implements IUIAnimationTimeline {
	protected UIAbstractAnimationTimeline(ITicker ticker) { super(ticker); }

	@Override
	public boolean append(IUIAnimationControl control) throws UIAlreadyInfiniteAnimationTimelineException {
		return append(control, LongUnaryOperator.identity());
	}

	@Override
	public boolean append(IUIAnimationControl control, long offset) throws UIAlreadyInfiniteAnimationTimelineException {
		return append(control, end -> offset);
	}
}
