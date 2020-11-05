package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.registries;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IJAXBObjectAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.registries.IJAXBObjectAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.ITuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.core.IRegistryObject;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.impl.AbstractTuple2Registry;

import java.util.Optional;

public abstract class AbstractJAXBObjectAdapterRegistry
		extends AbstractTuple2Registry<Class<?>, Class<?>, IJAXBObjectAdapter<?, ?>>
		implements IJAXBObjectAdapterRegistry {
	private static final long serialVersionUID = -3099502144881190092L;

	public AbstractJAXBObjectAdapterRegistry(boolean overridable) {
		super(overridable);
	}

	@Override
	public <L, R, VL extends IJAXBObjectAdapter<L, R>> IRegistryObject<VL> registerChecked(ITuple2<? extends Class<L>, ? extends Class<R>> key, VL value) {
		return register(key, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <L> Optional<? extends IRegistryObject<? extends IJAXBObjectAdapter<L, ?>>> getWithLeftChecked(Class<L> key) {
		return (Optional<? extends IRegistryObject<? extends IJAXBObjectAdapter<L, ?>>>) getWithLeft(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <L, R> Optional<? extends IRegistryObject<? extends IJAXBObjectAdapter<L, R>>> getChecked(ITuple2<? extends Class<L>, ? extends Class<R>> key) {
		return (Optional<? extends IRegistryObject<? extends IJAXBObjectAdapter<L, R>>>) get(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R> Optional<? extends IRegistryObject<? extends IJAXBObjectAdapter<?, R>>> getWithRightChecked(Class<R> key) {
		return (Optional<? extends IRegistryObject<? extends IJAXBObjectAdapter<?, R>>>) getWithRight(key);
	}

	@Override
	@Deprecated
	public Optional<? extends IRegistryObject<? extends IJAXBObjectAdapter<?, ?>>> get(ITuple2<? extends Class<?>, ? extends Class<?>> key) {
		return super.get(key);
	}

	@Override
	@Deprecated
	public <VL extends IJAXBObjectAdapter<?, ?>> IRegistryObject<VL> register(ITuple2<? extends Class<?>, ? extends Class<?>> key, VL value) {
		return super.register(key, value);
	}

	@Override
	@Deprecated
	public Optional<? extends IRegistryObject<? extends IJAXBObjectAdapter<?, ?>>> getWithLeft(Class<?> key) {
		return super.getWithLeft(key);
	}

	@Override
	@Deprecated
	public Optional<? extends IRegistryObject<? extends IJAXBObjectAdapter<?, ?>>> getWithRight(Class<?> key) {
		return super.getWithRight(key);
	}
}
