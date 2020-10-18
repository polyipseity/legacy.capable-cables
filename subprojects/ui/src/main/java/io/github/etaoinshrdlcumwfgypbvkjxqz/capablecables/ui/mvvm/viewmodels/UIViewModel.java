package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.viewmodels;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.models.IUIModel;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.viewmodels.IUIViewModel;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.viewmodels.IUIViewModelContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.UIAbstractSubInfrastructure;

public class UIViewModel<M extends IUIModel>
		extends UIAbstractSubInfrastructure<IUIViewModelContext>
		implements IUIViewModel<M> {
	private M model;

	public UIViewModel(M model) { this.model = model; }

	@Override
	public M getModel() { return model; }

	@Override
	public void setModel(M model) { this.model = model; }

	@Override
	public void initialize() {}

	@Override
	public void removed() {}
}
