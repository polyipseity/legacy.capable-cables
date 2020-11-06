package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.optionals.core;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface ICompositeOptional<T extends ICompositeOptional<T, V>, V extends ICompositeOptionalValues> {
	boolean isPresent();

	void ifPresent(Consumer<@Nonnull ? super V> consumer);

	T filter(Predicate<@Nonnull ? super V> predicate);

	<R> Optional<R> map(Function<@Nonnull ? super V, @Nullable ? extends R> mapper);

	<R> Optional<R> flatMap(Function<@Nonnull ? super V, @Nonnull ? extends Optional<? extends R>> mapper);
}
