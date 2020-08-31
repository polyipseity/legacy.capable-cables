package $group__.ui.core.mvvm.structures;

import $group__.utilities.interfaces.INamespacePrefixedString;
import org.w3c.dom.Node;

import java.util.Optional;

public interface IUIPropertyMappingValue {
	Optional<? extends Node> getDefaultValue();

	Optional<? extends INamespacePrefixedString> getBindingKey();
}
