package $group__.client.ui.mvvm.core.extensions;

import $group__.utilities.extensions.IExtension;
import $group__.utilities.extensions.IExtensionContainer;
import $group__.utilities.interfaces.INamespacePrefixedString;

public interface IUIExtension<K extends INamespacePrefixedString, C extends IExtensionContainer<? super K, ?>>
		extends IExtension<K, C> {}
