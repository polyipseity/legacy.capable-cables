package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.construction;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction.IUIViewArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;

import java.util.Map;

@Immutable
public final class UIImmutableViewArguments
		implements IUIViewArguments {
	private final Map<IIdentifier, IUIPropertyMappingValue> mappings;

	private UIImmutableViewArguments(Map<IIdentifier, IUIPropertyMappingValue> mappings) {
		this.mappings = ImmutableMap.copyOf(mappings);
	}

	public static UIImmutableViewArguments of(Map<IIdentifier, IUIPropertyMappingValue> mappings) {
		return new UIImmutableViewArguments(mappings);
	}

	@Override
	public @Immutable Map<IIdentifier, IUIPropertyMappingValue> getMappingsView() { return ImmutableMap.copyOf(mappings); }
}
