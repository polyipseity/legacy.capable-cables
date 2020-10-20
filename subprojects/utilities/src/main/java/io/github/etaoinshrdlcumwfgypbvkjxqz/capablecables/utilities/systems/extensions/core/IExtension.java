package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.IHasGenericClass;

public interface IExtension<K, C extends IExtensionContainer<? super K>>
		extends IHasGenericClass<C> {
	default void onExtensionAdded(C container) {}

	default void onExtensionRemoved() {}

	IExtensionType<K, ?, C> getType();
}
