package $group__.utilities;

import $group__.utilities.functions.IThrowingBiFunction;
import $group__.utilities.functions.IThrowingConsumer;
import $group__.utilities.functions.IThrowingSupplier;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class AutoCloseableRotator<T, TH extends Exception>
		implements AutoCloseable, IThrowingSupplier<Optional<? extends T>, TH> {
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
	public Optional<? extends T> get()
			throws TH {
		return Optional.ofNullable(getActiveReference().accumulateAndGet(getSupplier().get(),
				IThrowingBiFunction.<T, T, T, TH>execute((previous, next) -> {
					getCloser().accept(previous);
					return next;
				})::apply));
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
