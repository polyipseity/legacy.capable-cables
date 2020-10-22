package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.ITuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.core.IRegistryObject;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.core.ITuple2Registry;

import java.util.Map;
import java.util.Optional;

public abstract class AbstractTuple2Registry<KL, KR, V>
		extends AbstractRegistry<ITuple2<? extends KL, ? extends KR>, V>
		implements ITuple2Registry<KL, KR, V> {
	private static final long serialVersionUID = -1642550116959273162L;

	public AbstractTuple2Registry(@SuppressWarnings("SameParameterValue") boolean overridable) {
		super(overridable);
	}

	@Override
	public <VL extends V> IRegistryObject<VL> register(ITuple2<? extends KL, ? extends KR> key, VL value) {
		IRegistryObject<VL> ret = super.register(key, value);
		// COMMENT it will throw if it is not replaceable
		// TODO what if it is repeated
		getLeftData().put(key.getLeft(), key);
		getRightData().put(key.getRight(), key);
		return ret;
	}

	protected abstract Map<KL, ITuple2<? extends KL, ? extends KR>> getLeftData();

	protected abstract Map<KR, ITuple2<? extends KL, ? extends KR>> getRightData();

	@Override
	public Optional<? extends IRegistryObject<? extends V>> getWithLeft(KL key) {
		return Optional.ofNullable(getLeftData().get(key))
				.flatMap(this::get);
	}

	@Override
	public Optional<? extends IRegistryObject<? extends V>> getWithRight(KR key) {
		return Optional.ofNullable(getRightData().get(key))
				.flatMap(this::get);
	}
}
