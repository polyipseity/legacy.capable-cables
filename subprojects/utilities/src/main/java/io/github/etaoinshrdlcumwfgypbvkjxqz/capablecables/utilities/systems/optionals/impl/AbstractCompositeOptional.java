package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.optionals.impl;

import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.optionals.def.ICompositeOptional;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.optionals.def.ICompositeOptionalValues;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class AbstractCompositeOptional<T extends AbstractCompositeOptional<T, V>, V extends ICompositeOptionalValues>
		implements ICompositeOptional<T, V> {
	@SuppressWarnings({"UnstableApiUsage", "rawtypes", "RedundantSuppression"})
	@Override
	public boolean isPresent() {
		return Streams.stream(getValues().getSuppliers()).unordered()
				.map(Supplier::get)
				.allMatch(Objects::nonNull);
	}

	@Override
	public void ifPresent(Consumer<@Nonnull ? super V> consumer) {
		if (isPresent())
			consumer.accept(getValues());
	}

	@Override
	public T filter(Predicate<@Nonnull ? super V> predicate) {
		return isPresent() && predicate.test(getValues()) ? getThis() : getStaticEmpty();
	}

	@Override
	public <R> Optional<R> map(Function<@Nonnull ? super V, @Nullable ? extends R> mapper) {
		if (isPresent())
			return Optional.ofNullable(mapper.apply(getValues()));
		return Optional.empty();
	}

	@Override
	public <R> Optional<R> flatMap(Function<@Nonnull ? super V, @Nonnull ? extends Optional<? extends R>> mapper) {
		if (isPresent())
			return OptionalUtilities.upcast(mapper.apply(getValues()));
		return Optional.empty();
	}

	protected abstract T getThis();

	protected abstract T getStaticEmpty();

	protected abstract V getValues();
}
