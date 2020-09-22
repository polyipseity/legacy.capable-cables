package $group__.utilities.extensions.core;

import $group__.utilities.interfaces.IHasGenericClass;

public interface IExtension<K, C extends IExtensionContainer<? super K>>
		extends IHasGenericClass<C> {
	default void onExtensionAdded(C container) {}

	default void onExtensionRemoved() {}

	IExtensionType<K, ?, C> getType();
}
