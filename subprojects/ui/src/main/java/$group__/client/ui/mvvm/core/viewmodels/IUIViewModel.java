package $group__.client.ui.mvvm.core.viewmodels;

import $group__.client.ui.mvvm.core.binding.IHasBinding;
import $group__.client.ui.mvvm.core.models.IUIModel;

// TODO need to consider about threading
public interface IUIViewModel<M extends IUIModel> extends IHasBinding {
	M getModel();

	void setModel(M model);
}
