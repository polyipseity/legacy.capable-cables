package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.fields;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AbstractDelegatingObject;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.fields.IObservableField;

import java.util.Optional;

public class DelegatingBindingField<T>
		extends AbstractDelegatingObject<IBindingField<T>>
		implements IBindingField<T> {
	public DelegatingBindingField(IBindingField<T> delegate) {
		super(delegate);
	}

	@Override
	public IObservableField<T> getField() {
		return getDelegate().getField();
	}

	@Override
	public Optional<? extends IIdentifier> getBindingKey() {
		return getDelegate().getBindingKey();
	}
}
