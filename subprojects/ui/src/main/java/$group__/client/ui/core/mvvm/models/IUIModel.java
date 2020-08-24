package $group__.client.ui.core.mvvm.models;

import $group__.client.ui.core.mvvm.extensions.IUIExtension;
import $group__.utilities.extensions.IExtensionContainer;
import $group__.utilities.interfaces.INamespacePrefixedString;

public interface IUIModel
		extends IExtensionContainer<INamespacePrefixedString, IUIExtension<INamespacePrefixedString, ? super IUIModel>> {}
