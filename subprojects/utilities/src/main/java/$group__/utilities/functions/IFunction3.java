package $group__.utilities.functions;

import java.util.function.Function;

@FunctionalInterface
public interface IFunction3<T1, T2, T3, R> {
	default <V> IFunction3<T1, T2, T3, V> andThen(Function<? super R, ? extends V> after) {
		return (t1, t2, t3) -> after.apply(apply(t1, t2, t3));
	}

	R apply(T1 t1, T2 t2, T3 t3);
}
