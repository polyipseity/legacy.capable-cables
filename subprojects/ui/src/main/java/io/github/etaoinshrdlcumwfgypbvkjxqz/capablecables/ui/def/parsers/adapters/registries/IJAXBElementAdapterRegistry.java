package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.registries;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.IJAXBElementAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.tuples.ITuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.def.ICheckedTuple2Registry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.def.IRegistryObject;

import javax.xml.namespace.QName;
import java.util.Optional;

public interface IJAXBElementAdapterRegistry
		extends ICheckedTuple2Registry<QName, Class<?>, IJAXBElementAdapter<?, ?>> {
	long serialVersionUID = -1898139702002928271L;

	<L, R, VL extends IJAXBElementAdapter<L, R>> IRegistryObject<VL> registerChecked(ITuple2<? extends QName, ? extends Class<R>> key, VL value);

	Optional<? extends IRegistryObject<? extends IJAXBElementAdapter<?, ?>>> getWithLeftChecked(QName key);

	<L, R> Optional<? extends IRegistryObject<? extends IJAXBElementAdapter<L, R>>> getChecked(ITuple2<? extends QName, ? extends Class<R>> key);

	<R> Optional<? extends IRegistryObject<? extends IJAXBElementAdapter<?, R>>> getWithRightChecked(Class<R> key);
}
