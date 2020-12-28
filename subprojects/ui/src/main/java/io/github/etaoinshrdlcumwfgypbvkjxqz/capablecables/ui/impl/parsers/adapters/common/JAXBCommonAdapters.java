package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.common;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.registries.IJAXBAdapterRegistry;

public enum JAXBCommonAdapters {
	;

	public static void registerAll(IJAXBAdapterRegistry registry) {
		// COMMENT default
		EnumJAXBDefaultObjectAdapter.registerAll(registry);
		EnumJAXBDefaultElementAdapter.registerAll(registry);
		// COMMENT structure
		EnumJAXBStructureElementAdapter.registerAll(registry);
		// COMMENT collection
		EnumJAXBCollectionElementAdapter.registerAll(registry);
	}
}
