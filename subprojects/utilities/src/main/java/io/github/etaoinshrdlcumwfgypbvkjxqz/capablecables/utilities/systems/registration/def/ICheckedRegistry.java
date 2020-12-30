package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.def;

import java.util.Optional;

public interface ICheckedRegistry<K, V>
		extends IRegistry<K, V> {
	long serialVersionUID = 4762395485197492488L;

	@Override
	@Deprecated
	<VE extends V> IRegistryObject<VE> register(K key, VE value);

	@Override
	@Deprecated
	Optional<? extends IRegistryObject<? extends V>> get(K key);
}
