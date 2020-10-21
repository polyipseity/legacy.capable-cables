package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.JAXBAdapterRegistries;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IDuplexFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.ITuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples.ImmutableTuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.RegistryObject;

import java.io.Serializable;

@SuppressWarnings("unused")
public enum EnumJAXBObjectPresetAdapter
		implements ITuple2<ITuple2<? extends Class<?>, ? extends Class<?>>, RegistryObject<? extends IDuplexFunction<?, ?>>> {
	;

	private final ITuple2<ITuple2<? extends Class<?>, ? extends Class<?>>, RegistryObject<? extends IDuplexFunction<?, ?>>> delegate;

	<L, R, V extends IDuplexFunction<L, R> & Serializable> EnumJAXBObjectPresetAdapter(ITuple2<? extends Class<L>, ? extends Class<R>> key, V value) {
		RegistryObject<V> value2 = JAXBAdapterRegistries.Object.getInstance().registerChecked(ITuple2.upcast(key), value);
		this.delegate = ImmutableTuple2.of(key, value2);
	}

	@SuppressWarnings("EmptyMethod")
	public static void initializeClass() {}

	@Override
	public ITuple2<? extends Class<?>, ? extends Class<?>> getLeft() {
		return getDelegate().getLeft();
	}

	protected ITuple2<? extends ITuple2<? extends Class<?>, ? extends Class<?>>, ? extends RegistryObject<? extends IDuplexFunction<?, ?>>> getDelegate() {
		return delegate;
	}

	@Override
	public RegistryObject<? extends IDuplexFunction<?, ?>> getRight() {
		return getDelegate().getRight();
	}

	@Override
	public Object get(int index) throws IndexOutOfBoundsException {
		return getDelegate().get(index);
	}
}
