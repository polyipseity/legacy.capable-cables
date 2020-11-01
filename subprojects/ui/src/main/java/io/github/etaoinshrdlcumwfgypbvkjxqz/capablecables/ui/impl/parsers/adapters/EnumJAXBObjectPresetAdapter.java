package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.JAXBAdapterRegistries;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IDuplexFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.ITuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples.ImmutableTuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.core.IRegistryObject;

import java.awt.*;
import java.io.Serializable;

@SuppressWarnings("unused")
public enum EnumJAXBObjectPresetAdapter
		implements ITuple2<ITuple2<? extends Class<?>, ? extends Class<?>>, IRegistryObject<? extends IDuplexFunction<?, ?>>> {
	COLOR(ImmutableTuple2.of(io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.Color.class, Color.class),
			new IDuplexFunction.Functional<>(
					io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.Color::toJava,
					io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.Color::fromJava
			)),
	;

	private final ITuple2<ITuple2<? extends Class<?>, ? extends Class<?>>, IRegistryObject<? extends IDuplexFunction<?, ?>>> delegate;

	<L, R, V extends IDuplexFunction<L, R> & Serializable> EnumJAXBObjectPresetAdapter(ITuple2<? extends Class<L>, ? extends Class<R>> key, V value) {
		IRegistryObject<V> value2 = JAXBAdapterRegistries.Object.getInstance().registerChecked(key, value);
		this.delegate = ImmutableTuple2.of(key, value2);
	}

	@SuppressWarnings("EmptyMethod")
	public static void initializeClass() {}

	@Override
	public ITuple2<? extends Class<?>, ? extends Class<?>> getLeft() {
		return getDelegate().getLeft();
	}

	@Override
	public IRegistryObject<? extends IDuplexFunction<?, ?>> getRight() {
		return getDelegate().getRight();
	}

	protected ITuple2<? extends ITuple2<? extends Class<?>, ? extends Class<?>>, ? extends IRegistryObject<? extends IDuplexFunction<?, ?>>> getDelegate() {
		return delegate;
	}

	@Override
	public Object get(int index) throws IndexOutOfBoundsException {
		return getDelegate().get(index);
	}
}
