package $group__.utilities.binding.core.traits;

import $group__.utilities.interfaces.INamespacePrefixedString;

import java.util.Optional;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IHasBindingKey {
	Optional<? extends INamespacePrefixedString> getBindingKey();
}
