package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.binding.traits;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;

import java.util.Map;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IHasBindingMap {
	Map<IIdentifier, IUIPropertyMappingValue> getMappingsView();
}
