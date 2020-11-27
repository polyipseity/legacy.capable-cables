package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.registries.IJAXBAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;

import java.util.Map;
import java.util.Optional;

public interface IJAXBAdapterContext {
	static void assertDataIntegrity(Map<Class<?>, ?> data) {
		assert data.entrySet().stream().unordered()
				.allMatch(entry -> AssertionUtilities.assertNonnull(entry.getKey()).isInstance(entry.getValue()));
	}

	IJAXBAdapterRegistry getRegistry();

	<T> Optional<? extends T> getDatum(Class<T> key);

	IJAXBAdapterContext withData(Map<Class<?>, ?> data);

	@Immutable Map<Class<?>, ?> getDataView();
}
