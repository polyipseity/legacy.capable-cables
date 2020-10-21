package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.JAXBAdapterRegistries;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IDuplexFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.ITuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples.ImmutableTuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.Registry;

import java.io.Serializable;

public enum EnumJAXBObjectPresetAdapter
		implements ITuple2<Class<?>, Registry.RegistryObject<IDuplexFunction<?, ?>>> {
	;

	private final ITuple2<Class<?>, Registry.RegistryObject<IDuplexFunction<?, ?>>> delegate;

	<L, V extends IDuplexFunction<L, ?> & Serializable> EnumJAXBObjectPresetAdapter(Class<L> key, V value) {
		Registry.RegistryObject<IDuplexFunction<?, ?>> value2 = CastUtilities.castUnchecked(JAXBAdapterRegistries.Object.getInstance().registerSafe(key, value));
		this.delegate = ImmutableTuple2.of(key, value2);
	}

	@SuppressWarnings("EmptyMethod")
	public static void initializeClass() {}

	@Override
	public Class<?> getLeft() {
		return getDelegate().getLeft();
	}

	protected ITuple2<Class<?>, Registry.RegistryObject<IDuplexFunction<?, ?>>> getDelegate() {
		return delegate;
	}

	@Override
	public Registry.RegistryObject<IDuplexFunction<?, ?>> getRight() {
		return getDelegate().getRight();
	}

	@Override
	public Object get(int index) throws IndexOutOfBoundsException {
		return getDelegate().get(index);
	}
}
