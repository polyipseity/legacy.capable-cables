package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IIdentifier;

import java.util.Map;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public
interface IUIViewComponentArguments {
	@Immutable
	Map<IIdentifier, IUIPropertyMappingValue> getMappingsView();
}
