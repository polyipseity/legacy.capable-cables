package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.traits;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;

import java.util.Optional;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IHasBindingKey {
	Optional<? extends INamespacePrefixedString> getBindingKey();
}
