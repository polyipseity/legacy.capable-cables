package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions;

@FunctionalInterface
public interface IFunction4<T1, T2, T3, T4, R, T extends Throwable> {
	default <V> IFunction4<T1, T2, T3, T4, V, T> andThen(IThrowingFunction<? super R, ? extends V, ? extends T> after) {
		return (t1, t2, t3, t4) -> after.apply(apply(t1, t2, t3, t4));
	}

	R apply(T1 t1, T2 t2, T3 t3, T4 t4)
			throws T;
}
