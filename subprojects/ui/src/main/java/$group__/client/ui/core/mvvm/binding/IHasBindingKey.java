package $group__.client.ui.mvvm.core.binding;

import $group__.utilities.interfaces.INamespacePrefixedString;

import java.util.Optional;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IHasBindingKey {
	Optional<INamespacePrefixedString> getBindingKey();
}
