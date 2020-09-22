package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions;

@FunctionalInterface
public interface IConsumer3<T1, T2, T3, T extends Throwable> {
	default IConsumer3<T1, T2, T3, T> andThen(IConsumer3<? super T1, ? super T2, ? super T3, ? extends T> after) {
		return (t1, t2, t3) -> {
			accept(t1, t2, t3);
			after.accept(t1, t2, t3);
		};
	}

	void accept(T1 t1, T2 t2, T3 t3)
			throws T;

	enum StaticHolder {
		;

		private static final IConsumer3<Object, Object, Object, Throwable> EMPTY = (t1, t2, t3) -> {};

		@SuppressWarnings("unchecked")
		public static <T1, T2, T3, T extends Throwable> IConsumer3<T1, T2, T3, T> empty() {
			return (IConsumer3<T1, T2, T3, T>) EMPTY; // COMMENT always safe, accepts Object
		}
	}
}
