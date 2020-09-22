package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.adapters;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.JAXBAdapterRegistries;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IDuplexFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.Registry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Map;

public enum EnumJAXBObjectPresetAdapter
		implements Map.Entry<Class<?>, Registry.RegistryObject<IDuplexFunction<?, ?>>> {
	;

	protected final Class<?> key;
	protected final Registry.RegistryObject<IDuplexFunction<?, ?>> value;

	<L, V extends IDuplexFunction<L, ?> & Serializable> EnumJAXBObjectPresetAdapter(Class<L> key, V value) {
		this.key = key;
		this.value = CastUtilities.castUnchecked(JAXBAdapterRegistries.Object.INSTANCE.registerSafe(key, value));
	}

	@SuppressWarnings("EmptyMethod")
	public static void initializeClass() {}

	@Nonnull
	@Override
	public Class<?> getKey() { return key; }

	@Nonnull
	@Override
	public Registry.RegistryObject<IDuplexFunction<?, ?>> getValue() { return value; }

	@Nullable
	@Override
	public Registry.RegistryObject<IDuplexFunction<?, ?>> setValue(Registry.RegistryObject<IDuplexFunction<?, ?>> value)
			throws UnsupportedOperationException { throw new UnsupportedOperationException(); }
}
