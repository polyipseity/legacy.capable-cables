package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.registries;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.IJAXBObjectAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.tuples.ITuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.def.ICheckedTuple2Registry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.def.IRegistryObject;

import java.util.Optional;

public interface IJAXBObjectAdapterRegistry
		extends ICheckedTuple2Registry<Class<?>, Class<?>, IJAXBObjectAdapter<?, ?>> {
	long serialVersionUID = 7687136666644572368L;

	<L, R, VL extends IJAXBObjectAdapter<L, R>> IRegistryObject<VL> registerChecked(ITuple2<? extends Class<L>, ? extends Class<R>> key, VL value);

	<L> Optional<? extends IRegistryObject<? extends IJAXBObjectAdapter<L, ?>>> getWithLeftChecked(Class<L> key);

	<L, R> Optional<? extends IRegistryObject<? extends IJAXBObjectAdapter<L, R>>> getChecked(ITuple2<? extends Class<L>, ? extends Class<R>> key);

	<R> Optional<? extends IRegistryObject<? extends IJAXBObjectAdapter<?, R>>> getWithRightChecked(Class<R> key);
}
