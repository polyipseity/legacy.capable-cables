package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.fields;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IObservableField;

import java.util.Optional;

public final class ImmutableBindingField<T>
		implements IBindingField<T> {
	@Nullable
	private final INamespacePrefixedString bindingKey;
	private final IObservableField<T> field;

	private ImmutableBindingField(@Nullable INamespacePrefixedString bindingKey, IObservableField<T> field) {
		this.bindingKey = bindingKey;
		this.field = field;
	}

	public static <T> ImmutableBindingField<T> of(@Nullable INamespacePrefixedString bindingKey, IObservableField<T> field) {
		return new ImmutableBindingField<>(bindingKey, field);
	}

	@Override
	public IObservableField<T> getField() { return field; }

	@Override
	public Optional<? extends INamespacePrefixedString> getBindingKey() { return Optional.ofNullable(bindingKey); }
}
