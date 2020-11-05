package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.registries.IJAXBAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.registries.DefaultJAXBAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.registries.DefaultJAXBElementAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.registries.DefaultJAXBObjectAdapterRegistry;

@Deprecated // TODO remove
public enum LegacyJAXBAdapterRegistry {
	;

	private static final IJAXBAdapterRegistry REGISTRY;

	static {
		REGISTRY = new DefaultJAXBAdapterRegistry(new DefaultJAXBObjectAdapterRegistry(), new DefaultJAXBElementAdapterRegistry());
		EnumJAXBObjectPresetAdapter.registerAll(getRegistry());
		EnumJAXBElementPresetAdapter.registerAll(getRegistry());
	}

	public static IJAXBAdapterRegistry getRegistry() {
		return REGISTRY;
	}
}
