package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.def;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.tuples.ITuple2;

import java.util.Optional;

public interface ICheckedTuple2Registry<KL, KR, V>
		extends ITuple2Registry<KL, KR, V>, ICheckedRegistry<ITuple2<? extends KL, ? extends KR>, V> {
	@Override
	@Deprecated
	Optional<? extends IRegistryObject<? extends V>> getWithLeft(KL key);

	@Override
	@Deprecated
	Optional<? extends IRegistryObject<? extends V>> getWithRight(KR key);
}
