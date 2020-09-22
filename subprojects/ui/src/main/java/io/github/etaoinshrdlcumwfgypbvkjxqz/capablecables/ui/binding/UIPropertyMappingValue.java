package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.binding;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.INamespacePrefixedString;

import javax.annotation.Nullable;
import java.util.Optional;

public class UIPropertyMappingValue implements IUIPropertyMappingValue {
	@Nullable
	protected final Object defaultValue;
	@Nullable
	protected final INamespacePrefixedString bindingKey;

	public UIPropertyMappingValue(@Nullable Object defaultValue, @Nullable INamespacePrefixedString bindingKey) {
		this.defaultValue = defaultValue;
		this.bindingKey = bindingKey;
	}

	@Override
	public Optional<?> getDefaultValue() { return Optional.ofNullable(defaultValue); }

	@Override
	public Optional<? extends INamespacePrefixedString> getBindingKey() { return Optional.ofNullable(bindingKey); }
}
