package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def;

import java.util.Optional;

@FunctionalInterface
public interface IValue<T> {
	Optional<? extends T> getValue();
}
