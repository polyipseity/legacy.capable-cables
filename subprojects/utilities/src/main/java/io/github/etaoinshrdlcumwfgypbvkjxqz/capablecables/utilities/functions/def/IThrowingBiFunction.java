package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.def;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;

import java.util.function.BiFunction;

@FunctionalInterface
public interface IThrowingBiFunction<T, U, R, TH extends Throwable> {
	static <T, U, R, TH extends Throwable> BiFunction<T, U, R> executeNow(@Nonnull IThrowingBiFunction<T, U, R, TH> lambda)
			throws TH {
		return (t, u) -> {
			try {
				return lambda.apply(t, u);
			} catch (Throwable th) {
				throw ThrowableUtilities.propagateUnverified(th);
			}
		};
	}

	@Nullable
	R apply(@Nullable T t, @Nullable U u)
			throws TH;
}
