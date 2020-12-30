package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;

import java.util.Map;

public interface IUIRendererArguments {
	@Immutable
	Map<IIdentifier, IUIPropertyMappingValue> getMappingsView();

	Class<?> getContainerClass();
}
