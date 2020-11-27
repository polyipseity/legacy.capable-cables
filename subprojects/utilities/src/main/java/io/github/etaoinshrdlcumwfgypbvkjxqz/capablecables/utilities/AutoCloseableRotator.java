package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.core.IThrowingBiFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.core.IThrowingConsumer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.core.IThrowingSupplier;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class AutoCloseableRotator<T, TH extends Exception>
		implements AutoCloseable, IThrowingSupplier<T, TH> {
	private final Supplier<@Nonnull ? extends T> supplier;
	private final IThrowingConsumer<? super T, ? extends TH> closer;
	private final AtomicReference<T> activeReference = new AtomicReference<>(null);

	public AutoCloseableRotator(Supplier<@Nonnull ? extends T> supplier, IThrowingConsumer<? super T, ? extends TH> closer) {
		this.supplier = supplier;
		this.closer = closer;
	}

	public static <T extends AutoCloseable> AutoCloseableRotator<T, Exception> create(Supplier<@Nonnull ? extends T> supplier) {
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
				IThrowingBiFunction.<T, T, T, TH>executeNow((previous, next) -> {
					close();
					return next;
				})::apply
		);
	}

	protected Supplier<@Nonnull ? extends T> getSupplier() { return supplier; }

	@Override
	public void close()
			throws TH {
		Optional.ofNullable(getActiveReference().getAndSet(null))
				.ifPresent(IThrowingConsumer.executeNow(getCloser()));
	}

	protected IThrowingConsumer<? super T, ? extends TH> getCloser() { return closer; }
}
