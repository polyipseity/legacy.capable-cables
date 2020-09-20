package $group__.ui.core.binding.traits;

import $group__.ui.core.binding.IUIPropertyMappingValue;
import $group__.utilities.structures.INamespacePrefixedString;

import java.util.Map;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IHasBindingMap {
	Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappingsView();
}
