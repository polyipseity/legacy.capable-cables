package $group__.utilities.extensions.core;

import java.util.Optional;

public interface IExtensionType<K, V extends IExtension<K, C>, C extends IExtensionContainer<? super K>> {
	Optional<? extends V> find(C container);

	K getKey();
}
