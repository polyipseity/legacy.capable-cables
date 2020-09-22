package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces;

import javax.annotation.Nullable;
import java.util.Optional;

@FunctionalInterface
public interface IValue<T> {
	static <T> IValue<T> of(@Nullable T value) {
		Optional<T> r = Optional.ofNullable(value); // COMMENT no need to recompute
		return () -> r;
	}

	Optional<? extends T> getValue();
}
