package $group__.ui.core.mvvm.models;

import $group__.ui.core.mvvm.extensions.IUIExtension;
import $group__.utilities.extensions.IExtensionContainer;
import $group__.utilities.interfaces.INamespacePrefixedString;

public interface IUIModel
		extends IExtensionContainer<INamespacePrefixedString, IUIExtension<INamespacePrefixedString, ? super IUIModel>> {}
