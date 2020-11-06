package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.registries;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IJAXBElementAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.registries.IJAXBElementAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.ITuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.core.IRegistryObject;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.impl.AbstractTuple2Registry;

import javax.xml.namespace.QName;
import java.util.Optional;

public abstract class JAXBAbstractElementAdapterRegistry
		extends AbstractTuple2Registry<QName, Class<?>, IJAXBElementAdapter<?, ?>>
		implements IJAXBElementAdapterRegistry {
	private static final long serialVersionUID = -3099502144881190092L;

	public JAXBAbstractElementAdapterRegistry(boolean overridable) {
		super(overridable);
	}

	@Override
	public <L, R, VL extends IJAXBElementAdapter<L, R>> IRegistryObject<VL> registerChecked(ITuple2<? extends QName, ? extends Class<R>> key, VL value) {
		return register(key, value);
	}

	@Override
	public Optional<? extends IRegistryObject<? extends IJAXBElementAdapter<?, ?>>> getWithLeftChecked(QName key) {
		return getWithLeft(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <L, R> Optional<? extends IRegistryObject<? extends IJAXBElementAdapter<L, R>>> getChecked(ITuple2<? extends QName, ? extends Class<R>> key) {
		return (Optional<? extends IRegistryObject<? extends IJAXBElementAdapter<L, R>>>) get(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R> Optional<? extends IRegistryObject<? extends IJAXBElementAdapter<?, R>>> getWithRightChecked(Class<R> key) {
		return (Optional<? extends IRegistryObject<? extends IJAXBElementAdapter<?, R>>>) getWithRight(key);
	}

	@Override
	@Deprecated
	public Optional<? extends IRegistryObject<? extends IJAXBElementAdapter<?, ?>>> get(ITuple2<? extends QName, ? extends Class<?>> key) {
		return super.get(key);
	}

	@Override
	@Deprecated
	public <VL extends IJAXBElementAdapter<?, ?>> IRegistryObject<VL> register(ITuple2<? extends QName, ? extends Class<?>> key, VL value) {
		return super.register(key, value);
	}

	@Override
	@Deprecated
	public Optional<? extends IRegistryObject<? extends IJAXBElementAdapter<?, ?>>> getWithLeft(QName key) {
		return super.getWithLeft(key);
	}

	@Override
	@Deprecated
	public Optional<? extends IRegistryObject<? extends IJAXBElementAdapter<?, ?>>> getWithRight(Class<?> key) {
		return super.getWithRight(key);
	}
}
