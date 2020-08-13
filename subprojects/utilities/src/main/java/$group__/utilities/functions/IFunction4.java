package $group__.utilities.functions;

import java.util.function.Function;

@FunctionalInterface
public interface IFunction4<T1, T2, T3, T4, R> {
	default <V> IFunction4<T1, T2, T3, T4, V> andThen(Function<? super R, ? extends V> after) {
		return (t1, t2, t3, t4) -> after.apply(apply(t1, t2, t3, t4));
	}

	R apply(T1 t1, T2 t2, T3 t3, T4 t4);
}
