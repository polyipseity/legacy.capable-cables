package $group__.client.ui.mvvm.structures;

import $group__.client.ui.mvvm.core.structures.IUIPropertyMappingValue;

import javax.annotation.Nullable;
import java.util.Optional;

public class UIPropertyMappingValue implements IUIPropertyMappingValue {
	@Nullable
	protected final String defaultValue;
	@Nullable
	protected final String bindingString;

	public UIPropertyMappingValue(@Nullable String defaultValue, @Nullable String bindingString) {
		this.defaultValue = defaultValue;
		this.bindingString = bindingString;
	}

	@Override
	public Optional<String> getDefaultValue() { return Optional.ofNullable(defaultValue); }

	@Override
	public Optional<String> getBindingString() { return Optional.ofNullable(bindingString); }
}
