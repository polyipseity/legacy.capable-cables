package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.def;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;

import java.util.function.Predicate;

@FunctionalInterface
public interface IThrowingPredicate<T, TH extends Throwable> {
	static <T, TH extends Throwable> Predicate<T> executeNow(IThrowingPredicate<T, TH> lambda)
			throws TH {
		return t -> {
			try {
				return lambda.test(t);
			} catch (Throwable th) {
				throw ThrowableUtilities.propagateUnverified(th);
			}
		};
	}

	boolean test(T t)
			throws TH;
}
