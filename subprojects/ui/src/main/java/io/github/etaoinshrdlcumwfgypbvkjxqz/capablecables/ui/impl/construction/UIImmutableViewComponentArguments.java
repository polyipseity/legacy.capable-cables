package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.construction;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.IUIViewComponentArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;

import java.util.Map;

@Immutable
public final class UIImmutableViewComponentArguments
		implements IUIViewComponentArguments {
	private final Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings;

	public UIImmutableViewComponentArguments(Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings) {
		this.mappings = ImmutableMap.copyOf(mappings);
	}

	@Override
	public @Immutable Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappingsView() { return ImmutableMap.copyOf(mappings); }
}
