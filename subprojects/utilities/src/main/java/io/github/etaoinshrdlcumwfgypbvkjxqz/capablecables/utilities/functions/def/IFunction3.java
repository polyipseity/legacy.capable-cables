package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.def;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.MaybeNullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;

@FunctionalInterface
public interface IFunction3<T1, T2, T3, R, T extends Throwable> {
	static <T1, T2, T3, R, T extends Throwable> IFunction3<T1, T2, T3, R, RuntimeException> executeNow(IFunction3<T1, T2, T3, R, T> lambda)
			throws T {
		return (t1, t2, t3) -> {
			try {
				return lambda.apply(t1, t2, t3);
			} catch (Throwable t) {
				throw ThrowableUtilities.propagateUnverified(t);
			}
		};
	}

	@MaybeNullable
	R apply(@MaybeNullable T1 t1, @MaybeNullable T2 t2, @MaybeNullable T3 t3)
			throws T;

	default <V> IFunction3<T1, T2, T3, V, T> andThen(IThrowingFunction<? super R, ? extends V, ? extends T> after) {
		return (t1, t2, t3) -> after.apply(apply(t1, t2, t3));
	}

	enum StaticHolder {
		;

		private static final IFunction3<?, ?, ?, ?, ?> EMPTY = (t1, t2, t3) -> null;

		@SuppressWarnings("unchecked")
		public static <T1, T2, T3, R, T extends Throwable> IFunction3<T1, T2, T3, R, T> getEmpty() {
			return (IFunction3<T1, T2, T3, R, T>) EMPTY; // COMMENT always safe
		}

	}
}
