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

	private UIImmutableViewComponentArguments(Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings) {
		this.mappings = ImmutableMap.copyOf(mappings);
	}

	public static UIImmutableViewComponentArguments of(Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings) {
		return new UIImmutableViewComponentArguments(mappings);
	}

	@Override
	public @Immutable Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappingsView() { return ImmutableMap.copyOf(mappings); }
}
