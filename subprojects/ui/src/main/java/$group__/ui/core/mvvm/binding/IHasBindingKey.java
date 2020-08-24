package $group__.ui.core.mvvm.binding;

import $group__.utilities.interfaces.INamespacePrefixedString;

import java.util.Optional;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IHasBindingKey {
	Optional<INamespacePrefixedString> getBindingKey();
}
