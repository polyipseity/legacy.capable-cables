package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.IJAXBAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.registries.IJAXBAdapterRegistry;

import java.util.Map;
import java.util.Optional;

public final class JAXBImmutableAdapterContext
		implements IJAXBAdapterContext {
	private final IJAXBAdapterRegistry registry;
	private final @Immutable Map<Class<?>, ?> data;

	private JAXBImmutableAdapterContext(IJAXBAdapterRegistry registry, Map<? extends Class<?>, ?> data) {
		this.registry = registry;
		this.data = ImmutableMap.copyOf(data);

		IJAXBAdapterContext.assertDataIntegrity(this.data);
	}

	public static JAXBImmutableAdapterContext of(IJAXBAdapterRegistry registry) {
		return of(registry, ImmutableMap.of());
	}

	@Override
	public IJAXBAdapterRegistry getRegistry() {
		return registry;
	}

	@Override
	public <T> Optional<? extends T> getDatum(Class<T> key) {
		return Optional.ofNullable(key.cast(getData().get(key)));
	}

	@Override
	public IJAXBAdapterContext withData(Map<? extends Class<?>, ?> data) {
		return of(getRegistry(), data);
	}

	public static JAXBImmutableAdapterContext of(IJAXBAdapterRegistry registry, Map<? extends Class<?>, ?> data) {
		return new JAXBImmutableAdapterContext(registry, data);
	}

	protected Map<Class<?>, ?> getData() {
		return data;
	}

	@Override
	public @Immutable Map<Class<?>, ?> getDataView() {
		return ImmutableMap.copyOf(getData());
	}
}
