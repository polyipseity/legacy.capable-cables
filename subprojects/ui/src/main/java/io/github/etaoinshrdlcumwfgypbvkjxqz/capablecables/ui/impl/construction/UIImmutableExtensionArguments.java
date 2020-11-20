package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.construction;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.IUIExtensionArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;

import java.util.Map;

@Immutable
public final class UIImmutableExtensionArguments
		implements IUIExtensionArguments {
	private final Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings;
	private final Class<?> containerClass;

	public UIImmutableExtensionArguments(Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings,
	                                     Class<?> containerClass) {
		this.mappings = ImmutableMap.copyOf(mappings);
		this.containerClass = containerClass;
	}

	@Override
	public @Immutable Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappingsView() { return ImmutableMap.copyOf(mappings); }

	@Override
	public Class<?> getContainerClass() { return containerClass; }
}
