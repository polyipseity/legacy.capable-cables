package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.viewmodels.IUIViewModelContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.IUIViewContext;

public interface IUIContextContainer {
	@Immutable
	IUIViewContext getViewContext();

	@Immutable
	IUIViewModelContext getViewModelContext();
}
