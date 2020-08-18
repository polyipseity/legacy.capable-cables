package $group__.client.ui.mvvm.core.structures;

import net.minecraft.util.ResourceLocation;
import org.w3c.dom.Node;

import java.util.Optional;

public interface IUIPropertyMappingValue {
	Optional<Node> getDefaultValue();

	Optional<ResourceLocation> getBindingKey();
}
