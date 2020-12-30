package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.def;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.MaybeNullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;

import java.util.function.Function;

@FunctionalInterface
public interface IThrowingFunction<T, R, TH extends Throwable> {
	@SuppressWarnings("RedundantThrows")
	static <T, R, TH extends Throwable> Function<T, R> executeNow(IThrowingFunction<T, R, TH> lambda)
			throws TH {
		return t -> {
			try {
				return lambda.apply(t);
			} catch (Throwable th) {
				throw ThrowableUtilities.propagateUnverified(th);
			}
		};
	}

	@MaybeNullable
	R apply(@MaybeNullable T t) throws TH;
}
