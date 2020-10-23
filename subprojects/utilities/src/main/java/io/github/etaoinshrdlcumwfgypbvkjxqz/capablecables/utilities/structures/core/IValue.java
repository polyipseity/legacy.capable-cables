package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core;

import java.util.Optional;

@FunctionalInterface
public interface IValue<T> {
	Optional<? extends T> getValue();
}
