package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.fields;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.fields.IObservableField;

import java.util.Optional;

public final class ImmutableBindingField<T>
		implements IBindingField<T> {
	@Nullable
	private final IIdentifier bindingKey;
	private final IObservableField<T> field;

	private ImmutableBindingField(@Nullable IIdentifier bindingKey, IObservableField<T> field) {
		this.bindingKey = bindingKey;
		this.field = field;
	}

	public static <T> ImmutableBindingField<T> of(@Nullable IIdentifier bindingKey, IObservableField<T> field) {
		return new ImmutableBindingField<>(bindingKey, field);
	}

	@Override
	public IObservableField<T> getField() { return field; }

	@Override
	public Optional<? extends IIdentifier> getBindingKey() { return Optional.ofNullable(bindingKey); }
}
