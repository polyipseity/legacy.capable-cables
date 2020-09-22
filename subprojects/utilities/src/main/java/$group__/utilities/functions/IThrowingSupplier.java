package $group__.utilities.functions;

import $group__.utilities.throwable.ThrowableUtilities;

import javax.annotation.Nullable;
import java.util.function.Supplier;

@FunctionalInterface
public interface IThrowingSupplier<V, TH extends Throwable> {
	@SuppressWarnings("RedundantThrows")
	static <V, TH extends Throwable> Supplier<V> execute(IThrowingSupplier<V, TH> lambda)
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
