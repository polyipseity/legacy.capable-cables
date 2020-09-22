package $group__.utilities.functions;

@FunctionalInterface
public interface IFunction3<T1, T2, T3, R, T extends Throwable> {
	default <V> IFunction3<T1, T2, T3, V, T> andThen(IThrowingFunction<? super R, ? extends V, ? extends T> after) {
		return (t1, t2, t3) -> after.apply(apply(t1, t2, t3));
	}

	R apply(T1 t1, T2 t2, T3 t3)
			throws T;
}
