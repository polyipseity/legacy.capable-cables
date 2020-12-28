package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.animations.UIImmutableAnimationTime;

import java.util.concurrent.TimeUnit;

public interface IUIAnimationTime {
	static IUIAnimationTime sum(IUIAnimationTime a, IUIAnimationTime b) {
		return a.isFinite() && b.isFinite() ? UIImmutableAnimationTime.of(a.get() + b.get()) : UIImmutableAnimationTime.getInfinity();
	}

	boolean isFinite();

	long get()
			throws IllegalStateException;

	static IUIAnimationTime max(IUIAnimationTime a, IUIAnimationTime b) {
		if (a.isInfinite())
			return a;
		if (b.isInfinite())
			return b;
		return a.get() > b.get() ? a : b;
	}

	boolean isInfinite();

	static IUIAnimationTime min(IUIAnimationTime a, IUIAnimationTime b) {
		if (a.isInfinite())
			return b;
		if (b.isInfinite())
			return a;
		return a.get() < b.get() ? a : b;
	}

	long get(TimeUnit timeUnit)
			throws IllegalStateException;
}
