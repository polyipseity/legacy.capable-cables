package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.core;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.MaybeNullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;

import java.util.function.ToIntFunction;

@FunctionalInterface
public interface IThrowingToIntFunction<T, TH extends Throwable> {
	@SuppressWarnings("RedundantThrows")
	static <T, TH extends Throwable> ToIntFunction<T> executeNow(IThrowingToIntFunction<T, TH> lambda)
			throws TH {
		return t -> {
			try {
				return lambda.apply(t);
			} catch (Throwable th) {
				throw ThrowableUtilities.propagateUnverified(th);
			}
		};
	}

	int apply(@MaybeNullable T t)
			throws TH;
}
