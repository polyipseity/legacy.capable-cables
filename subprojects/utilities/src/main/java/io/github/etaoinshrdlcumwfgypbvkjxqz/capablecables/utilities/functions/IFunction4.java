package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;

@FunctionalInterface
public interface IFunction4<T1, T2, T3, T4, R, T extends Throwable> {
	@SuppressWarnings("RedundantThrows")
	static <T1, T2, T3, T4, R, T extends Throwable> IFunction4<T1, T2, T3, T4, R, RuntimeException> executeNow(IFunction4<T1, T2, T3, T4, R, T> lambda)
			throws T {
		return (t1, t2, t3, t4) -> {
			try {
				return lambda.apply(t1, t2, t3, t4);
			} catch (Throwable t) {
				throw ThrowableUtilities.propagateUnverified(t);
			}
		};
	}

	@Nullable
	R apply(@Nullable T1 t1, @Nullable T2 t2, @Nullable T3 t3, @Nullable T4 t4)
			throws T;

	default <V> IFunction4<T1, T2, T3, T4, V, T> andThen(IThrowingFunction<? super R, ? extends V, ? extends T> after) {
		return (t1, t2, t3, t4) -> after.apply(apply(t1, t2, t3, t4));
	}
}
