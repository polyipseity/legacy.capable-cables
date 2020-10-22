package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions;

import com.google.common.collect.MapMaker;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.ITuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples.AbstractTuple2Registry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.RegistryObject;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.function.Consumer;

public abstract class AbstractDuplexFunctionRegistry
		extends AbstractTuple2Registry<Class<?>, Class<?>, IDuplexFunction<?, ?>> {
	private static final long serialVersionUID = 7632764233866904218L;

	public AbstractDuplexFunctionRegistry(@SuppressWarnings("SameParameterValue") boolean overridable, Logger logger, Consumer<? super MapMaker> configuration) {
		super(overridable, logger, configuration);
	}

	public <L, R, VL extends IDuplexFunction<L, R>> RegistryObject<VL> registerChecked(ITuple2<Class<L>, Class<R>> key, VL value) {
		return register(ITuple2.upcast(key), value);
	}

	@Override
	@Deprecated
	public <VL extends IDuplexFunction<?, ?>> RegistryObject<VL> register(ITuple2<Class<?>, Class<?>> key, VL value) {
		return super.register(key, value);
	}

	@Override
	@Deprecated
	public Optional<? extends RegistryObject<? extends IDuplexFunction<?, ?>>> getWithLeft(Class<?> key) {
		return super.getWithLeft(key);
	}

	@Override
	@Deprecated
	public Optional<? extends RegistryObject<? extends IDuplexFunction<?, ?>>> getWithRight(Class<?> key) {
		return super.getWithRight(key);
	}

	@SuppressWarnings("unchecked")
	public <L, R> Optional<? extends RegistryObject<? extends IDuplexFunction<L, R>>> getChecked(ITuple2<Class<L>, Class<R>> key) {
		return (Optional<? extends RegistryObject<? extends IDuplexFunction<L, R>>>) get(ITuple2.upcast(key));
	}

	@Override
	@Deprecated
	public Optional<? extends RegistryObject<? extends IDuplexFunction<?, ?>>> get(ITuple2<Class<?>, Class<?>> key) {
		return super.get(key);
	}

	@SuppressWarnings("unchecked")
	public <L> Optional<? extends RegistryObject<? extends IDuplexFunction<L, ?>>> getWithLeftChecked(Class<L> key) {
		return (Optional<? extends RegistryObject<? extends IDuplexFunction<L, ?>>>) getWithLeft(key);
	}

	@SuppressWarnings("unchecked")
	public <R> Optional<? extends RegistryObject<? extends IDuplexFunction<?, R>>> getWithRightChecked(Class<R> key) {
		return (Optional<? extends RegistryObject<? extends IDuplexFunction<?, R>>>) getWithRight(key);
	}
}
