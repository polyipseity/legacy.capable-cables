package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.throwable.ThrowableUtilities;

import javax.annotation.Nullable;

@FunctionalInterface
public interface IConsumer4<T1, T2, T3, T4, T extends Throwable> {
	default IConsumer4<T1, T2, T3, T4, T> andThen(IConsumer4<? super T1, ? super T2, ? super T3, ? super T4, ? extends T> after) {
		return (t1, t2, t3, t4) -> {
			accept(t1, t2, t3, t4);
			after.accept(t1, t2, t3, t4);
		};
	}

	void accept(@Nullable T1 t1, @Nullable T2 t2, @Nullable T3 t3, @Nullable T4 t4)
			throws T;

	enum StaticHolder {
		;

		@SuppressWarnings("RedundantThrows")
		public static <T1, T2, T3, T4, T extends Throwable> IConsumer4<T1, T2, T3, T4, RuntimeException> executeNow(IConsumer4<T1, T2, T3, T4, T> lambda)
				throws T {
			return (t1, t2, t3, t4) -> {
				try {
					lambda.accept(t1, t2, t3, t4);
				} catch (Throwable t) {
					throw ThrowableUtilities.propagateUnverified(t);
				}
			};
		}
	}
}
