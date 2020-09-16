package $group__.ui.core.mvvm.viewmodels;

import $group__.ui.core.mvvm.IUISubInfrastructure;
import $group__.ui.core.mvvm.models.IUIModel;
import $group__.utilities.binding.core.traits.IHasBinding;
import $group__.utilities.extensions.IExtensionContainer;
import $group__.utilities.interfaces.INamespacePrefixedString;

// TODO need to consider about threading
public interface IUIViewModel<M extends IUIModel>
		extends IUISubInfrastructure, IHasBinding, IExtensionContainer<INamespacePrefixedString> {
	M getModel();

	void setModel(M model);
}
