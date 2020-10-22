package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.core;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IDuplexFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.ITuple2;

import java.util.Optional;

public interface IClassBasedDuplexFunctionRegistry
		extends ITuple2Registry<Class<?>, Class<?>, IDuplexFunction<?, ?>> {
	<L, R, VL extends IDuplexFunction<L, R>> IRegistryObject<VL> registerChecked(ITuple2<? extends Class<L>, ? extends Class<R>> key, VL value);

	<L, R> Optional<? extends IRegistryObject<? extends IDuplexFunction<L, R>>> getChecked(ITuple2<? extends Class<L>, ? extends Class<R>> key);

	<L> Optional<? extends IRegistryObject<? extends IDuplexFunction<L, ?>>> getWithLeftChecked(Class<L> key);

	<R> Optional<? extends IRegistryObject<? extends IDuplexFunction<?, R>>> getWithRightChecked(Class<R> key);

	@Override
	@Deprecated
	Optional<? extends IRegistryObject<? extends IDuplexFunction<?, ?>>> getWithLeft(Class<?> key);

	@Override
	@Deprecated
	Optional<? extends IRegistryObject<? extends IDuplexFunction<?, ?>>> getWithRight(Class<?> key);

	@Override
	@Deprecated
	<VE extends IDuplexFunction<?, ?>> IRegistryObject<VE> register(ITuple2<? extends Class<?>, ? extends Class<?>> key, VE value);

	@Override
	@Deprecated
	Optional<? extends IRegistryObject<? extends IDuplexFunction<?, ?>>> get(ITuple2<? extends Class<?>, ? extends Class<?>> key);
}
