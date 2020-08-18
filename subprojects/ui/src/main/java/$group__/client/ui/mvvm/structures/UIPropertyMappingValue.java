package $group__.client.ui.mvvm.structures;

import $group__.client.ui.mvvm.core.structures.IUIPropertyMappingValue;
import net.minecraft.util.ResourceLocation;
import org.w3c.dom.Node;

import javax.annotation.Nullable;
import java.util.Optional;

public class UIPropertyMappingValue implements IUIPropertyMappingValue {
	@Nullable
	protected final Node defaultValue;
	@Nullable
	protected final ResourceLocation bindingKey;

	public UIPropertyMappingValue(@Nullable Node defaultValue, @Nullable ResourceLocation bindingKey) {
		this.defaultValue = defaultValue;
		this.bindingKey = bindingKey;
	}

	@Override
	public Optional<Node> getDefaultValue() { return Optional.ofNullable(defaultValue); }

	@Override
	public Optional<ResourceLocation> getBindingKey() { return Optional.ofNullable(bindingKey); }
}
