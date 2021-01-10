package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.def.IRegistryObjectInternal;

public class DefaultRegistryObject<V>
		implements IRegistryObjectInternal<V> {
	private static final long serialVersionUID = -7426757514591663232L;
	@SuppressWarnings("NonSerializableFieldInSerializableClass")
	private V value;

	public DefaultRegistryObject(V value) {
		this.value = value;
	}

	@Override
	public V getValue() {
		return value;
	}

	@Override
	public void setValue(V value) {
		this.value = value;
	}
}
