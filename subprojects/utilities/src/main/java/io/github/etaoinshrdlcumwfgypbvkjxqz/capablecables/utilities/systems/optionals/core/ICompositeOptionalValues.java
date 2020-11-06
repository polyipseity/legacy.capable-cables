package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.optionals.core;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;

import java.util.function.Supplier;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface ICompositeOptionalValues {
	Iterable<? extends Supplier<@Nullable ?>> getSuppliers();
}
