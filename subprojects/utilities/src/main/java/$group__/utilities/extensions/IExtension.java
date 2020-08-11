package $group__.utilities.extensions;

import $group__.utilities.interfaces.IHasGenericClass;

public interface IExtension<C extends IExtensionContainer<?, ?>> extends IHasGenericClass<C> {
	default void onExtensionAdded(C container) {}

	default void onExtensionRemoved() {}
}
