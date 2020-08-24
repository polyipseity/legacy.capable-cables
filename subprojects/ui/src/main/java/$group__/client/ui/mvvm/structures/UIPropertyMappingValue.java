package $group__.client.ui.mvvm.structures;

import $group__.client.ui.core.mvvm.structures.IUIPropertyMappingValue;
import $group__.utilities.interfaces.INamespacePrefixedString;
import org.w3c.dom.Node;

import javax.annotation.Nullable;
import java.util.Optional;

public class UIPropertyMappingValue implements IUIPropertyMappingValue {
	@Nullable
	protected final Node defaultValue;
	@Nullable
	protected final INamespacePrefixedString bindingKey;

	public UIPropertyMappingValue(@Nullable Node defaultValue, @Nullable INamespacePrefixedString bindingKey) {
		this.defaultValue = defaultValue;
		this.bindingKey = bindingKey;
	}

	@Override
	public Optional<Node> getDefaultValue() { return Optional.ofNullable(defaultValue); }

	@Override
	public Optional<INamespacePrefixedString> getBindingKey() { return Optional.ofNullable(bindingKey); }
}
