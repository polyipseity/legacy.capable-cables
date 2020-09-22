package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.traits;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.INamespacePrefixedString;

import java.util.Map;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IHasBindingMap {
	Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappingsView();
}
