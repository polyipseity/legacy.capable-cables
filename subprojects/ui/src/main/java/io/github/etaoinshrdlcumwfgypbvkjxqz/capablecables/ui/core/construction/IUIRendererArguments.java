package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;

import java.util.Map;

public interface IUIRendererArguments {
	@Immutable
	Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappingsView();

	Class<?> getContainerClass();
}
