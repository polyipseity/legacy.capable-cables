package $group__.client.ui.mvvm.core.viewmodels;

import $group__.client.ui.mvvm.core.IUICommon;
import $group__.client.ui.mvvm.core.binding.IHasBinding;
import $group__.client.ui.mvvm.core.extensions.IUIExtension;
import $group__.client.ui.mvvm.core.models.IUIModel;
import $group__.utilities.extensions.IExtensionContainer;
import $group__.utilities.interfaces.INamespacePrefixedString;

// TODO need to consider about threading
public interface IUIViewModel<M extends IUIModel>
		extends IUICommon, IHasBinding, IExtensionContainer<INamespacePrefixedString, IUIExtension<INamespacePrefixedString, ? super IUIViewModel<?>>> {
	M getModel();

	void setModel(M model);
}
