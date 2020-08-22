package $group__.utilities.functions;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public final class ConstantSupplier<T>
		implements Supplier<T> {
	private static final ConstantSupplier<?> NULL_SUPPLIER = new ConstantSupplier<>(null);
	@Nullable
	protected final T constant;

	protected ConstantSupplier(@Nullable T constant) { this.constant = constant; }

	public static <T> ConstantSupplier<T> of(@Nullable T constant) { return constant == null ? getNullSupplier() : new ConstantSupplier<>(constant); }

	@SuppressWarnings("unchecked")
	public static <T> ConstantSupplier<T> getNullSupplier() {
		return (ConstantSupplier<T>) NULL_SUPPLIER; // COMMENT always safe, returns null
	}

	@Override
	@Nullable
	public T get() { return getConstant(); }

	@Nullable
	protected T getConstant() { return constant; }
}
