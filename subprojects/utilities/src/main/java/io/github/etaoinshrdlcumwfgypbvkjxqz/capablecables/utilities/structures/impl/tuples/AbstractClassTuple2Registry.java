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

public abstract class AbstractClassTuple2Registry<V>
		extends AbstractRegistry<ITuple2<Class<?>, Class<?>>, V> {
	private static final long serialVersionUID = -1642550116959273162L;
	private final ConcurrentMap<Class<?>, ITuple2<Class<?>, Class<?>>> leftMap;
	private final ConcurrentMap<Class<?>, ITuple2<Class<?>, Class<?>>> rightMap;

	public AbstractClassTuple2Registry(@SuppressWarnings("SameParameterValue") boolean overridable,
	                                   Logger logger,
	                                   Consumer<? super MapMaker> configuration) {
		super(overridable, logger, configuration);
		MapMaker dataBuilder = FunctionUtilities.accept(new MapMaker(), configuration);
		this.leftMap = dataBuilder.makeMap();
		this.rightMap = dataBuilder.makeMap();
	}

	@Override
	public <VL extends V> RegistryObject<VL> register(ITuple2<Class<?>, Class<?>> key, VL value) {
		RegistryObject<VL> ret = super.register(key, value);
		// COMMENT it will throw if it is not replaceable
		getLeftMap().put(key.getLeft(), key);
		getRightMap().put(key.getRight(), key);
		return ret;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<Class<?>, ITuple2<Class<?>, Class<?>>> getLeftMap() {
		return leftMap;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<Class<?>, ITuple2<Class<?>, Class<?>>> getRightMap() {
		return rightMap;
	}

	public Optional<? extends RegistryObject<? extends V>> getWithLeft(Class<?> key) {
		return Optional.ofNullable(getLeftMap().get(key))
				.flatMap(this::get);
	}

	public Optional<? extends RegistryObject<? extends V>> getWithRight(Class<?> key) {
		return Optional.ofNullable(getRightMap().get(key))
				.flatMap(this::get);
	}
}
