package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;

import java.util.function.Consumer;

@FunctionalInterface
public interface IThrowingConsumer<T, TH extends Throwable> {
	@SuppressWarnings("RedundantThrows")
	static <T, TH extends Throwable> Consumer<T> executeNow(IThrowingConsumer<T, TH> lambda)
			throws TH {
		return t -> {
			try {
				lambda.accept(t);
			} catch (Throwable th) {
				throw ThrowableUtilities.propagateUnverified(th);
			}
		};
	}

	void accept(@Nullable T t) throws TH;
}
