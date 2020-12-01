package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.types;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.types.IConstantSupplier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AbstractDelegatingObject;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ConstantValue;

public final class ImmutableConstantSupplier<T>
		extends AbstractDelegatingObject<ConstantValue<T>>
		implements IConstantSupplier<T> {
	private ImmutableConstantSupplier(T value) {
		super(ConstantValue.of(value));
	}

	public static <T> ImmutableConstantSupplier<T> of(T value) {
		return new ImmutableConstantSupplier<>(value);
	}

	@Override
	public T get() {
		return getDelegate().get();
	}
}
