package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.binding;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IIdentifier;

import java.util.Optional;

public final class UIImmutablePropertyMappingValue implements IUIPropertyMappingValue {
	@Nullable
	private final Object defaultValue;
	@Nullable
	private final IIdentifier bindingKey;

	private UIImmutablePropertyMappingValue(@Nullable Object defaultValue, @Nullable IIdentifier bindingKey) {
		this.defaultValue = defaultValue;
		this.bindingKey = bindingKey;
	}

	public static UIImmutablePropertyMappingValue of(@Nullable Object defaultValue, @Nullable IIdentifier bindingKey) {
		return new UIImmutablePropertyMappingValue(defaultValue, bindingKey);
	}

	public static UIImmutablePropertyMappingValue ofValue(@Nullable Object value) {
		return of(value, null);
	}

	public static UIImmutablePropertyMappingValue ofKey(@Nullable IIdentifier bindingKey) {
		return of(null, bindingKey);
	}

	@Override
	public Optional<? extends IIdentifier> getBindingKey() { return Optional.ofNullable(bindingKey); }

	@Override
	public Optional<?> getDefaultValue() { return Optional.ofNullable(defaultValue); }
}
