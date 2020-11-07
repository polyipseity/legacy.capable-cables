package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.registries.IJAXBAdapterRegistry;

public enum JAXBUIAdapters {
	;

	public static void registerAll(IJAXBAdapterRegistry registry) {
		// COMMENT UI
		EnumJAXBUIDefaultObjectAdapter.registerAll(registry);
		EnumJAXBUIDefaultElementAdapter.registerAll(registry);
	}
}
