package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;

public interface IValueHolder<T>
		extends IValue<T> {
	void setValue(@Nullable T value);
}
