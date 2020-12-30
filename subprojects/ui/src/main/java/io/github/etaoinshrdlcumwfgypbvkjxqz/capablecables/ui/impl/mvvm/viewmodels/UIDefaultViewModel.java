package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.viewmodels;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.models.IUIModel;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.viewmodels.IUIViewModel;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.viewmodels.IUIViewModelContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.UIAbstractSubInfrastructure;

public class UIDefaultViewModel<M extends IUIModel>
		extends UIAbstractSubInfrastructure<IUIViewModelContext>
		implements IUIViewModel<M> {
	private M model;

	public UIDefaultViewModel(M model) { this.model = model; }

	@Override
	public M getModel() { return model; }

	@Override
	public void setModel(M model) { this.model = model; }
}
