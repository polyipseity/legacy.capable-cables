package $group__.utilities.functions;

@FunctionalInterface
public interface IConsumer3<T1, T2, T3> {
	default IConsumer3<T1, T2, T3> andThen(IConsumer3<? super T1, ? super T2, ? super T3> after) {
		return (t1, t2, t3) -> {
			accept(t1, t2, t3);
			after.accept(t1, t2, t3);
		};
	}

	void accept(T1 t1, T2 t2, T3 t3);
}
