package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.def;

import java.util.Optional;

public interface IExtensionType<K, V extends IExtension<K, C>, C extends IExtensionContainer<? super K>> {
	Optional<? extends V> find(C container);

	K getKey();
}
