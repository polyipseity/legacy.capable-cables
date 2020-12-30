package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.def;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;

import java.util.Optional;
import java.util.function.Consumer;

public interface IThrowableHandler<T extends Throwable>
		extends Consumer<@Nonnull T> {
	Optional<? extends T> get();

	void clear();

	@Override
	default void accept(@Nonnull T t) { catch_(t); }

	void catch_(T throwable);
}
