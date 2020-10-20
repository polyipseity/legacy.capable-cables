package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IDuplexFunction;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.function.Function;

public abstract class StandardDuplexFunctionRegistry
		extends Registry<Class<?>, IDuplexFunction<?, ?>> {
	protected StandardDuplexFunctionRegistry(@SuppressWarnings("SameParameterValue") boolean overridable, Logger logger) { super(overridable, logger); }

	public <L, VL extends IDuplexFunction<L, ?>> RegistryObject<VL> registerSafe(Class<L> key, VL value) { return register(key, value); }

	public <L, VL extends IDuplexFunction<L, ?>> RegistryObject<VL> registerApplySafe(Class<L> key, Function<? super Class<L>, ? extends VL> value) {
		return registerApply(
				key,
				CastUtilities.castUnchecked(value) // COMMENT should be safe
		);
	}

	@Override
	@Deprecated
	public <VL extends IDuplexFunction<?, ?>> RegistryObject<VL> registerApply(Class<?> key, Function<? super Class<?>, ? extends VL> value) { return super.registerApply(key, value); }

	@Override
	@Deprecated
	public <VL extends IDuplexFunction<?, ?>> RegistryObject<VL> register(Class<?> key, VL value) { return super.register(key, value); }

	@Override
	@Deprecated
	public Optional<? extends RegistryObject<? extends IDuplexFunction<?, ?>>> get(Class<?> key) { return super.get(key); }

	@SuppressWarnings("unchecked")
	public <L> Optional<? extends RegistryObject<? extends IDuplexFunction<L, ?>>> getSafe(Class<L> key) { return (Optional<? extends RegistryObject<? extends IDuplexFunction<L, ?>>>) get(key); }
}
