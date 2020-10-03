package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.throwable.ThrowableUtilities;

import javax.annotation.Nullable;

@FunctionalInterface
public interface IConsumer3<T1, T2, T3, T extends Throwable> {
	default IConsumer3<T1, T2, T3, T> andThen(IConsumer3<? super T1, ? super T2, ? super T3, ? extends T> after) {
		return (t1, t2, t3) -> {
			accept(t1, t2, t3);
			after.accept(t1, t2, t3);
		};
	}

	void accept(@Nullable T1 t1, @Nullable T2 t2, @Nullable T3 t3)
			throws T;

	enum StaticHolder {
		;

		private static final IConsumer3<?, ?, ?, ?> EMPTY = (t1, t2, t3) -> {};

		@SuppressWarnings("unchecked")
		public static <T1, T2, T3, T extends Throwable> IConsumer3<T1, T2, T3, T> getEmpty() {
			return (IConsumer3<T1, T2, T3, T>) EMPTY; // COMMENT always safe, accepts Object
		}

		@SuppressWarnings("RedundantThrows")
		public static <T1, T2, T3, T extends Throwable> IConsumer3<T1, T2, T3, RuntimeException> executeNow(IConsumer3<T1, T2, T3, T> lambda)
				throws T {
			return (t1, t2, t3) -> {
				try {
					lambda.accept(t1, t2, t3);
				} catch (Throwable t) {
					throw ThrowableUtilities.propagateUnverified(t);
				}
			};
		}
	}
}
