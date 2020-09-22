package $group__.ui.core.mvvm.extensions;

import $group__.utilities.extensions.core.IExtension;
import $group__.utilities.extensions.core.IExtensionContainer;
import $group__.utilities.structures.INamespacePrefixedString;

public interface IUIExtension<K extends INamespacePrefixedString, C extends IExtensionContainer<? super K>>
		extends IExtension<K, C> {}
