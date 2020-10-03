package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.optionals;

import java.util.function.Supplier;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface ICompositeOptionalValues {
	Iterable<? extends Supplier<?>> getSuppliers();
}
