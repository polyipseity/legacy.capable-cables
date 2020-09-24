package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions;

import java.util.function.Function;
import java.util.function.Supplier;

public class MappableSupplier<T>
		extends DelegatingSupplier<T> {
	public MappableSupplier(Supplier<T> delegated) { super(delegated); }

	@SuppressWarnings({"rawtypes", "RedundantSuppression"})
	public <R> MappableSupplier<R> flatMap(Function<? super T, ? extends Supplier<? extends R>> mapper) { return map(mapper.andThen(Supplier::get)); }

	public <R> MappableSupplier<R> map(Function<? super T, ? extends R> mapper) { return new MappableSupplier<>(() -> mapper.apply(get())); }
}
