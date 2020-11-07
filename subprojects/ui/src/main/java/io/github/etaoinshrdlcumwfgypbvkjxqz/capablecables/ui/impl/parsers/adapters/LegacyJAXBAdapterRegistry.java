package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.registries.IJAXBAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.common.JAXBCommonAdapters;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.registries.JAXBDefaultAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.registries.JAXBDefaultElementAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.registries.JAXBDefaultObjectAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui.JAXBUIAdapters;

@Deprecated // TODO remove
public enum LegacyJAXBAdapterRegistry {
	;

	private static final IJAXBAdapterRegistry REGISTRY;

	static {
		REGISTRY = new JAXBDefaultAdapterRegistry(new JAXBDefaultObjectAdapterRegistry(), new JAXBDefaultElementAdapterRegistry());
		JAXBCommonAdapters.registerAll(getRegistry());
		JAXBUIAdapters.registerAll(getRegistry());
	}

	public static IJAXBAdapterRegistry getRegistry() {
		return REGISTRY;
	}
}
