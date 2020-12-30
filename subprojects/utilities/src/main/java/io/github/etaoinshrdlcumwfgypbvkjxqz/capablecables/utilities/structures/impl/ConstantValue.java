package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.def.ICompatibilitySupplier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IValue;

import java.util.Optional;

public final class ConstantValue<T>
		implements ICompatibilitySupplier<T>, IValue<T> {
	private static final ConstantValue<?> EMPTY = new ConstantValue<>();
	@Nullable
	private final T constant;

	private ConstantValue() { this.constant = null; }

	private ConstantValue(T constant) { this.constant = AssertionUtilities.assertNonnull(constant); }

	public static <T> ConstantValue<T> of(@Nullable T constant) { return constant == null ? getEmpty() : new ConstantValue<>(constant); }

	@SuppressWarnings("unchecked")
	public static <T> ConstantValue<T> getEmpty() {
		return (ConstantValue<T>) EMPTY; // COMMENT always safe, returns null
	}

	@Override
	public Optional<? extends T> getValue() {
		return Optional.ofNullable(constant);
	}

	@Override
	@Nullable
	public T get() { return getConstant(); }

	@Nullable
	protected T getConstant() { return constant; }
}
