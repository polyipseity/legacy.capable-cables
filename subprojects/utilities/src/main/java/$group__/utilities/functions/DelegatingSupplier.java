package $group__.utilities.functions;

import $group__.utilities.interfaces.IDelegating;

import java.util.function.Supplier;

public class DelegatingSupplier<T>
		extends IDelegating.Impl<Supplier<T>>
		implements Supplier<T> {
	public DelegatingSupplier(Supplier<T> delegated) { super(delegated); }

	@Override
	public T get() { return getDelegated().get(); }
}
