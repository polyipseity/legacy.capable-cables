package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.animations.timelines;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.animations.AbstractUIAnimationPlayable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.AlreadyInfiniteUIAnimationTimelineException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationControl;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationTimeline;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.time.ITicker;

import java.util.function.LongUnaryOperator;

public abstract class AbstractUIAnimationTimeline
		extends AbstractUIAnimationPlayable
		implements IUIAnimationTimeline {
	protected AbstractUIAnimationTimeline(ITicker ticker) { super(ticker); }

	@Override
	public boolean append(IUIAnimationControl control) throws AlreadyInfiniteUIAnimationTimelineException {
		return append(control, LongUnaryOperator.identity());
	}

	@Override
	public boolean append(IUIAnimationControl control, long offset) throws AlreadyInfiniteUIAnimationTimelineException {
		return append(control, end -> offset);
	}
}
