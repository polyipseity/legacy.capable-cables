package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;

import java.util.Map;
import java.util.Optional;

public interface IUIExtensionArguments {
	@Immutable
	Map<IIdentifier, IUIPropertyMappingValue> getMappingsView();

	Class<?> getContainerClass();

	Optional<? extends String> getRendererName();
}
