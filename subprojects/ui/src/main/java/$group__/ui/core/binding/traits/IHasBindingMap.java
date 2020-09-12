package $group__.ui.core.binding.traits;

import $group__.ui.core.mvvm.structures.IUIPropertyMappingValue;
import $group__.utilities.interfaces.INamespacePrefixedString;

import java.util.Map;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IHasBindingMap {
	Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappingsView();
}
