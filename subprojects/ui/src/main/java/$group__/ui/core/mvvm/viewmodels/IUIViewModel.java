package $group__.ui.core.mvvm.viewmodels;

import $group__.ui.core.mvvm.IUICommon;
import $group__.ui.core.mvvm.binding.IHasBinding;
import $group__.ui.core.mvvm.extensions.IUIExtension;
import $group__.ui.core.mvvm.models.IUIModel;
import $group__.utilities.extensions.IExtensionContainer;
import $group__.utilities.interfaces.INamespacePrefixedString;

// TODO need to consider about threading
public interface IUIViewModel<M extends IUIModel>
		extends IUICommon, IHasBinding, IExtensionContainer<INamespacePrefixedString, IUIExtension<INamespacePrefixedString, ? super IUIViewModel<?>>> {
	M getModel();

	void setModel(M model);
}
