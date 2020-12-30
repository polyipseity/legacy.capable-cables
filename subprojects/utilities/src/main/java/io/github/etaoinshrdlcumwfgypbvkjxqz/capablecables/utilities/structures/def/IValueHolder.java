package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;

public interface IValueHolder<T>
		extends IValue<T> {
	void setValue(@Nullable T value);
}
