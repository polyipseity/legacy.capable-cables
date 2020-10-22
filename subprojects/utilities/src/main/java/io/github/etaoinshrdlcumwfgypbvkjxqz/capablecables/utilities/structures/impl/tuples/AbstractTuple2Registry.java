package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples;

import com.google.common.collect.MapMaker;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.FunctionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.ITuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.AbstractRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.RegistryObject;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

public abstract class AbstractTuple2Registry<KL, KR, V>
		extends AbstractRegistry<ITuple2<KL, KR>, V> {
	private static final long serialVersionUID = -1642550116959273162L;
	private final ConcurrentMap<KL, ITuple2<KL, KR>> leftMap;
	private final ConcurrentMap<KR, ITuple2<KL, KR>> rightMap;

	public AbstractTuple2Registry(@SuppressWarnings("SameParameterValue") boolean overridable,
	                              Logger logger,
	                              Consumer<? super MapMaker> configuration) {
		super(overridable, logger, configuration);
		MapMaker dataBuilder = FunctionUtilities.accept(new MapMaker(), configuration);
		this.leftMap = dataBuilder.makeMap();
		this.rightMap = dataBuilder.makeMap();
	}

	@Override
	public <VL extends V> RegistryObject<VL> register(ITuple2<KL, KR> key, VL value) {
		RegistryObject<VL> ret = super.register(key, value);
		// COMMENT it will throw if it is not replaceable
		// TODO what if it is repeated
		getLeftMap().put(key.getLeft(), key);
		getRightMap().put(key.getRight(), key);
		return ret;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<KL, ITuple2<KL, KR>> getLeftMap() {
		return leftMap;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<KR, ITuple2<KL, KR>> getRightMap() {
		return rightMap;
	}

	public Optional<? extends RegistryObject<? extends V>> getWithLeft(KL key) {
		return Optional.ofNullable(getLeftMap().get(key))
				.flatMap(this::get);
	}

	public Optional<? extends RegistryObject<? extends V>> getWithRight(KR key) {
		return Optional.ofNullable(getRightMap().get(key))
				.flatMap(this::get);
	}
}
