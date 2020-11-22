package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IValueHolder;

import java.util.Optional;

public class DefaultValueHolder<T>
		implements IValueHolder<T> {
	private @Nullable T value;

	@Override
	public Optional<? extends T> getValue() {
		return Optional.ofNullable(value);
	}

	@Override
	public void setValue(@Nullable T value) {
		this.value = value;
	}
}
