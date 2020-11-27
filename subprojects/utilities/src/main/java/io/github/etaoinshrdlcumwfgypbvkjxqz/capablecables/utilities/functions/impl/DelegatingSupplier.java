package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AbstractDelegatingObject;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.core.ICompatibilitySupplier;

import java.util.function.Supplier;

public class DelegatingSupplier<T>
		extends AbstractDelegatingObject<Supplier<T>>
		implements ICompatibilitySupplier<T> {
	public DelegatingSupplier(Supplier<T> delegated) { super(delegated); }

	@Override
	@Nullable
	public T get() { return getDelegate().get(); }
}
