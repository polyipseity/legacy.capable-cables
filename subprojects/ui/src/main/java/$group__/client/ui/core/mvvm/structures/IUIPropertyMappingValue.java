package $group__.client.ui.mvvm.core.structures;

import $group__.utilities.interfaces.INamespacePrefixedString;
import org.w3c.dom.Node;

import java.util.Optional;

public interface IUIPropertyMappingValue {
	Optional<Node> getDefaultValue();

	Optional<INamespacePrefixedString> getBindingKey();
}
