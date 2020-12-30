package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.optionals.def;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;

import java.util.Iterator;
import java.util.function.Supplier;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface ICompositeOptionalValues {
	Iterator<? extends Supplier<@Nullable ?>> getSuppliers();
}
