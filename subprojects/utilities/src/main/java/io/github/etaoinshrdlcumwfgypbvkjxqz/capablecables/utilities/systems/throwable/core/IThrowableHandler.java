package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.core;

import java.util.Optional;
import java.util.function.Consumer;

public interface IThrowableHandler<T extends Throwable>
		extends Consumer<T> {
	Optional<? extends T> get();

	void clear();

	@Override
	default void accept(T t) { catch_(t); }

	void catch_(T throwable);
}
