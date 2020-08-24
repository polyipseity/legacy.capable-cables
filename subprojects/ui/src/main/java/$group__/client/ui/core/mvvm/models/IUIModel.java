package $group__.client.ui.mvvm.core.models;

import $group__.client.ui.mvvm.core.extensions.IUIExtension;
import $group__.utilities.extensions.IExtensionContainer;
import $group__.utilities.interfaces.INamespacePrefixedString;

public interface IUIModel
		extends IExtensionContainer<INamespacePrefixedString, IUIExtension<INamespacePrefixedString, ? super IUIModel>> {}
