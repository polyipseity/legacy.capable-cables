package $group__.ui.binding;

import $group__.ui.core.binding.IUIPropertyMappingValue;
import $group__.utilities.interfaces.INamespacePrefixedString;

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
