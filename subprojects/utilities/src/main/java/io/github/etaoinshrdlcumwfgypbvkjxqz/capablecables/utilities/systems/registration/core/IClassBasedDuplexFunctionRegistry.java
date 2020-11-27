package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.core;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.core.IDuplexFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.ITuple2;

import java.util.Optional;

public interface IClassBasedDuplexFunctionRegistry
		extends ICheckedTuple2Registry<Class<?>, Class<?>, IDuplexFunction<?, ?>> {
	long serialVersionUID = -8925960207653959885L;

	<L, R, VL extends IDuplexFunction<L, R>> IRegistryObject<VL> registerChecked(ITuple2<? extends Class<L>, ? extends Class<R>> key, VL value);

	<L, R> Optional<? extends IRegistryObject<? extends IDuplexFunction<L, R>>> getChecked(ITuple2<? extends Class<L>, ? extends Class<R>> key);

	<L> Optional<? extends IRegistryObject<? extends IDuplexFunction<L, ?>>> getWithLeftChecked(Class<L> key);

	<R> Optional<? extends IRegistryObject<? extends IDuplexFunction<?, R>>> getWithRightChecked(Class<R> key);
}
