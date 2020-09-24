package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IThrowingBiFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IThrowingConsumer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IThrowingSupplier;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class AutoCloseableRotator<T, TH extends Exception>
		implements AutoCloseable, IThrowingSupplier<T, TH> {
	private final Supplier<? extends T> supplier;
	private final IThrowingConsumer<? super T, ? extends TH> closer;
	private final AtomicReference<T> activeReference = new AtomicReference<>(null);

	public AutoCloseableRotator(Supplier<? extends T> supplier, IThrowingConsumer<? super T, ? extends TH> closer) {
		this.supplier = supplier;
		this.closer = closer;
	}

	public static <T extends AutoCloseable> AutoCloseableRotator<T, Exception> create(Supplier<? extends T> supplier) {
		return new AutoCloseableRotator<>(supplier, AutoCloseable::close);
	}

	public Optional<? extends T> getActive() { return Optional.ofNullable(getActiveReference().get()); }

	protected AtomicReference<T> getActiveReference() { return activeReference; }

	@Override
	@Nonnull
	public T get()
			throws TH {
		return getActiveReference().accumulateAndGet(
				AssertionUtilities.assertNonnull(getSupplier().get()),
				IThrowingBiFunction.<T, T, T, TH>execute((previous, next) -> {
					close();
					return next;
				})::apply
		);
	}

	protected Supplier<? extends T> getSupplier() { return supplier; }

	@Override
	public void close()
			throws TH {
		Optional.ofNullable(getActiveReference().getAndSet(null))
				.ifPresent(IThrowingConsumer.execute(getCloser()));
	}

	protected IThrowingConsumer<? super T, ? extends TH> getCloser() { return closer; }
}
