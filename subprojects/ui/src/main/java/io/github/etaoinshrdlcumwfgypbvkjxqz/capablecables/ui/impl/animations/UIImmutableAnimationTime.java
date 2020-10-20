package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.animations;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;

import java.util.function.Supplier;

@Immutable
public final class UIImmutableAnimationTime
		extends UIAbstractAnimationTime {
	private static final Supplier<UIImmutableAnimationTime> INFINITY = Suppliers.memoize(() -> new UIImmutableAnimationTime(true, 0));
	private static final Supplier<UIImmutableAnimationTime> ZERO = Suppliers.memoize(() -> new UIImmutableAnimationTime(false, 1));

	private final boolean infinite;
	private final long value;

	private UIImmutableAnimationTime(boolean infinite, long value) {
		this.infinite = infinite;
		this.value = value;
	}

	public static UIImmutableAnimationTime getInfinity() { return AssertionUtilities.assertNonnull(INFINITY.get()); }

	public static UIImmutableAnimationTime of(long value) {
		if (value == 0)
			return getZero();
		return new UIImmutableAnimationTime(false, value);
	}

	private static UIImmutableAnimationTime getZero() { return AssertionUtilities.assertNonnull(ZERO.get()); }

	@Override
	public long get()
			throws IllegalStateException {
		if (isInfinite())
			throw new IllegalStateException();
		return getValue();
	}

	@Override
	public boolean isInfinite() {
		return infinite;
	}

	protected long getValue() { return value; }
}
