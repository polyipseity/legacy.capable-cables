package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IJAXBAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.registries.IJAXBAdapterRegistry;

public final class ImmutableJAXBAdapterContext
		implements IJAXBAdapterContext {
	private final IJAXBAdapterRegistry registry;

	private ImmutableJAXBAdapterContext(IJAXBAdapterRegistry registry) {
		this.registry = registry;
	}

	public static ImmutableJAXBAdapterContext of(IJAXBAdapterRegistry registry) {
		return new ImmutableJAXBAdapterContext(registry);
	}

	@Override
	public IJAXBAdapterRegistry getRegistry() {
		return registry;
	}
}
