package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.animations;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationTime;

import java.util.concurrent.TimeUnit;

public abstract class UIAbstractAnimationTime
		implements IUIAnimationTime {
	@Override
	public boolean isFinite() { return !isInfinite(); }

	@Override
	public long get(TimeUnit timeUnit) throws IllegalStateException {
		return timeUnit.convert(get(), TimeUnit.NANOSECONDS);
	}
}
