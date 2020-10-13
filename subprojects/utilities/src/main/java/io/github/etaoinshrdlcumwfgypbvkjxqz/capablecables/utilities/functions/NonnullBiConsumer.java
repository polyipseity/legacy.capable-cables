package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;

@FunctionalInterface
public interface NonnullBiConsumer<T, U>
		extends BiConsumer<T, U> {
	@Override
	@Deprecated
	default void accept(@Nullable T t, @Nullable U u) {
		assert t != null;
		assert u != null;
		acceptNonnull(t, u);
	}

	void acceptNonnull(T t, U u);
}
