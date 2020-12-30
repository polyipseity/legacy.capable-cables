package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.animations;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.animations.IUIAnimationTime;

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
