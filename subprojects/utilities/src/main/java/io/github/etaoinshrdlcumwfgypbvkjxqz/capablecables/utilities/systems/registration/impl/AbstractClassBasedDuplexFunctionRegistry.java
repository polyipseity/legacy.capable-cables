package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IDuplexFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.ITuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.core.IClassBasedDuplexFunctionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.core.IRegistryObject;

import java.util.Optional;

public abstract class AbstractClassBasedDuplexFunctionRegistry
		extends AbstractTuple2Registry<Class<?>, Class<?>, IDuplexFunction<?, ?>>
		implements IClassBasedDuplexFunctionRegistry {
	private static final long serialVersionUID = 7632764233866904218L;

	public AbstractClassBasedDuplexFunctionRegistry(@SuppressWarnings("SameParameterValue") boolean overridable) {
		super(overridable);
	}

	@Override
	public <L, R, VL extends IDuplexFunction<L, R>> IRegistryObject<VL> registerChecked(ITuple2<? extends Class<L>, ? extends Class<R>> key, VL value) {
		return register(key, value);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <L, R> Optional<? extends IRegistryObject<? extends IDuplexFunction<L, R>>> getChecked(ITuple2<? extends Class<L>, ? extends Class<R>> key) {
		return (Optional<? extends IRegistryObject<? extends IDuplexFunction<L, R>>>) get(key);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <L> Optional<? extends IRegistryObject<? extends IDuplexFunction<L, ?>>> getWithLeftChecked(Class<L> key) {
		return (Optional<? extends IRegistryObject<? extends IDuplexFunction<L, ?>>>) getWithLeft(key);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <R> Optional<? extends IRegistryObject<? extends IDuplexFunction<?, R>>> getWithRightChecked(Class<R> key) {
		return (Optional<? extends IRegistryObject<? extends IDuplexFunction<?, R>>>) getWithRight(key);
	}

	@Override
	@Deprecated
	public Optional<? extends IRegistryObject<? extends IDuplexFunction<?, ?>>> get(ITuple2<? extends Class<?>, ? extends Class<?>> key) {
		return super.get(key);
	}

	@Override
	@Deprecated
	public <VL extends IDuplexFunction<?, ?>> IRegistryObject<VL> register(ITuple2<? extends Class<?>, ? extends Class<?>> key, VL value) {
		return super.register(key, value);
	}

	@Override
	@Deprecated
	public Optional<? extends IRegistryObject<? extends IDuplexFunction<?, ?>>> getWithLeft(Class<?> key) {
		return super.getWithLeft(key);
	}

	@Override
	@Deprecated
	public Optional<? extends IRegistryObject<? extends IDuplexFunction<?, ?>>> getWithRight(Class<?> key) {
		return super.getWithRight(key);
	}
}
