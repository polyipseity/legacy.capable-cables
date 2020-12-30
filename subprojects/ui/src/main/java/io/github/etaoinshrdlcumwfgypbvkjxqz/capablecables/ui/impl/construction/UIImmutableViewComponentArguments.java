package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.construction;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction.IUIViewComponentArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;

import java.util.Map;

@Immutable
public final class UIImmutableViewComponentArguments
		implements IUIViewComponentArguments {
	private final Map<IIdentifier, IUIPropertyMappingValue> mappings;

	private UIImmutableViewComponentArguments(Map<IIdentifier, IUIPropertyMappingValue> mappings) {
		this.mappings = ImmutableMap.copyOf(mappings);
	}

	public static UIImmutableViewComponentArguments of(Map<IIdentifier, IUIPropertyMappingValue> mappings) {
		return new UIImmutableViewComponentArguments(mappings);
	}

	@Override
	public @Immutable Map<IIdentifier, IUIPropertyMappingValue> getMappingsView() { return ImmutableMap.copyOf(mappings); }
}
