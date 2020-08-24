package $group__.client.ui.core.mvvm.viewmodels;

import $group__.client.ui.core.mvvm.IUICommon;
import $group__.client.ui.core.mvvm.binding.IHasBinding;
import $group__.client.ui.core.mvvm.extensions.IUIExtension;
import $group__.client.ui.core.mvvm.models.IUIModel;
import $group__.utilities.extensions.IExtensionContainer;
import $group__.utilities.interfaces.INamespacePrefixedString;

// TODO need to consider about threading
public interface IUIViewModel<M extends IUIModel>
		extends IUICommon, IHasBinding, IExtensionContainer<INamespacePrefixedString, IUIExtension<INamespacePrefixedString, ? super IUIViewModel<?>>> {
	M getModel();

	void setModel(M model);
}
