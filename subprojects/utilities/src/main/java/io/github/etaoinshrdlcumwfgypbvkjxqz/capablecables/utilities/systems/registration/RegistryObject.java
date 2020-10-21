package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration;

import java.io.Serializable;
import java.util.function.Supplier;

public final class RegistryObject<V>
		implements Serializable, Supplier<V> {
	private static final long serialVersionUID = -7426757514591663232L;
	@SuppressWarnings("NonSerializableFieldInSerializableClass")
	private V value;

	public RegistryObject(V value) { this.value = value;}

	@Override
	public V get() { return getValue(); }

	public V getValue() { return value; }

	protected void setValue(V value) { this.value = value; }
}
