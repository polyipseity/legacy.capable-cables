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

	private UIImmutablePropertyMappingValue(@Nullable Object defaultValue, @Nullable INamespacePrefixedString bindingKey) {
		this.defaultValue = defaultValue;
		this.bindingKey = bindingKey;
	}

	public static UIImmutablePropertyMappingValue of(@Nullable Object defaultValue, @Nullable INamespacePrefixedString bindingKey) {
		return new UIImmutablePropertyMappingValue(defaultValue, bindingKey);
	}

	@Override
	public Optional<? extends INamespacePrefixedString> getBindingKey() { return Optional.ofNullable(bindingKey); }

	@Override
	public Optional<?> getDefaultValue() { return Optional.ofNullable(defaultValue); }
}
