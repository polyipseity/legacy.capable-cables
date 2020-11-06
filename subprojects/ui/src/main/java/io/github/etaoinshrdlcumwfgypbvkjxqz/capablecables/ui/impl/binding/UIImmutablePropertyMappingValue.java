package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.binding;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;

import java.util.Optional;

public final class UIImmutablePropertyMappingValue implements IUIPropertyMappingValue {
	@Nullable
	private final Object defaultValue;
	@Nullable
	private final INamespacePrefixedString bindingKey;

	public UIImmutablePropertyMappingValue(@Nullable Object defaultValue, @Nullable INamespacePrefixedString bindingKey) {
		this.defaultValue = defaultValue;
		this.bindingKey = bindingKey;
	}

	@Override
	public Optional<?> getDefaultValue() { return Optional.ofNullable(defaultValue); }

	@Override
	public Optional<? extends INamespacePrefixedString> getBindingKey() { return Optional.ofNullable(bindingKey); }
}
