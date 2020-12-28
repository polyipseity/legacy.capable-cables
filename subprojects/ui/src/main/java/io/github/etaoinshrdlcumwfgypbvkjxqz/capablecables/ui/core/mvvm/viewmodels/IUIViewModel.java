package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.viewmodels;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUISubInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.models.IUIModel;

public interface IUIViewModel<M extends IUIModel>
		extends IUISubInfrastructure<IUIViewModelContext> {
	M getModel();

	void setModel(M model);
}
