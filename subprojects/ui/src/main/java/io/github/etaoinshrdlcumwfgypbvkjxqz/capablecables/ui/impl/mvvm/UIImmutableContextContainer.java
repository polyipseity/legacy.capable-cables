package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.IUIContextContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.viewmodels.IUIViewModelContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.IUIViewContext;

public final class UIImmutableContextContainer
		implements IUIContextContainer {
	private final IUIViewContext viewContext;
	private final IUIViewModelContext viewModelContext;

	public UIImmutableContextContainer(IUIViewContext viewContext, IUIViewModelContext viewModelContext) {
		this.viewContext = viewContext;
		this.viewModelContext = viewModelContext;
	}

	@Override
	public @Immutable IUIViewContext getViewContext() { return viewContext; }

	@Override
	public @Immutable IUIViewModelContext getViewModelContext() { return viewModelContext; }
}
