package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.optionals.core;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface ICompositeOptional<T extends ICompositeOptional<T, V>, V extends ICompositeOptionalValues> {
	boolean isPresent();

	void ifPresent(Consumer<? super V> consumer);

	T filter(Predicate<? super V> predicate);

	<R> Optional<R> map(Function<? super V, ? extends R> mapper);

	<R> Optional<R> flatMap(Function<? super V, ? extends Optional<? extends R>> mapper);
}
