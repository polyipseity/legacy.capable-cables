package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.def;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;

import java.util.function.Supplier;

@FunctionalInterface
public interface IThrowingSupplier<V, TH extends Throwable> {
	@SuppressWarnings("RedundantThrows")
	static <V, TH extends Throwable> Supplier<V> executeNow(IThrowingSupplier<V, TH> lambda)
			throws TH {
		return () -> {
			try {
				return lambda.get();
			} catch (Throwable th) {
				throw ThrowableUtilities.propagateUnverified(th);
			}
		};
	}

	@Nullable
	V get() throws TH;
}
