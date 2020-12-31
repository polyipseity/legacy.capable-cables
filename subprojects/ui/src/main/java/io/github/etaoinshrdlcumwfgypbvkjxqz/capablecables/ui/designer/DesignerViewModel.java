package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.designer;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.viewmodels.UIDefaultViewModel;

public class DesignerViewModel
		extends UIDefaultViewModel<DesignerModel> {
	protected DesignerViewModel(DesignerModel model) {
		super(model);
	}

	public static DesignerViewModel of(DesignerModel model) {
		return new DesignerViewModel(model);
	}
}
