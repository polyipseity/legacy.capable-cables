package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.throwable.ThrowableUtilities;

import java.util.function.Function;

@FunctionalInterface
public interface IThrowingFunction<T, R, TH extends Throwable> {
	@SuppressWarnings("RedundantThrows")
	static <T, R, TH extends Throwable> Function<T, R> execute(IThrowingFunction<T, R, TH> lambda)
			throws TH {
		return t -> {
			try {
				return lambda.apply(t);
			} catch (Throwable th) {
				throw ThrowableUtilities.propagateUnverified(th);
			}
		};
	}

	R apply(T t) throws TH;
}
