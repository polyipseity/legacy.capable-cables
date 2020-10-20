package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.optionals.core;

import java.util.function.Supplier;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface ICompositeOptionalValues {
	Iterable<? extends Supplier<?>> getSuppliers();
}
