package $group__.utilities.binding.core.traits;

import $group__.utilities.structures.INamespacePrefixedString;

import java.util.Optional;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IHasBindingKey {
	Optional<? extends INamespacePrefixedString> getBindingKey();
}
