package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;

import javax.annotation.Nullable;

public final class ConstantSupplier<T>
		implements ICompatibilitySupplier<T> {
	private static final ConstantSupplier<?> EMPTY = new ConstantSupplier<>();
	@Nullable
	private final T constant;

	protected ConstantSupplier() { this.constant = null; }

	protected ConstantSupplier(T constant) { this.constant = AssertionUtilities.assertNonnull(constant); }

	public static <T> ConstantSupplier<T> of(@Nullable T constant) { return constant == null ? getEmpty() : new ConstantSupplier<>(constant); }

	@SuppressWarnings("unchecked")
	public static <T> ConstantSupplier<T> getEmpty() {
		return (ConstantSupplier<T>) EMPTY; // COMMENT always safe, returns null
	}

	@Override
	@Nullable
	public T get() { return getConstant(); }

	@Nullable
	protected T getConstant() { return constant; }
}
