package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.viewmodels;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.IUISubInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.models.IUIModel;

public interface IUIViewModel<M extends IUIModel>
		extends IUISubInfrastructure<IUIViewModelContext> {
	M getModel();

	void setModel(M model);
}
