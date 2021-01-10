package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.def;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import org.reactivestreams.Publisher;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

public interface IRegistry<K, V>
		extends Serializable {
	<VE extends V> IRegistryObject<VE> register(K key, VE value);

	boolean isOverridable();

	Optional<? extends IRegistryObject<? extends V>> get(K key);

	boolean containsKey(Object key);

	boolean containsValue(Object value);
}
