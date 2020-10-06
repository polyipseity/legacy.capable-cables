package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUIContextContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.viewmodels.IUIViewModelContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;

public final class ImmutableUIContextContainer
		implements IUIContextContainer {
	private final IUIViewContext viewContext;
	private final IUIViewModelContext viewModelContext;

	public ImmutableUIContextContainer(IUIViewContext viewContext, IUIViewModelContext viewModelContext) {
		this.viewContext = viewContext;
		this.viewModelContext = viewModelContext;
	}

	@Override
	public @Immutable IUIViewContext getViewContext() { return viewContext; }

	@Override
	public @Immutable IUIViewModelContext getViewModelContext() { return viewModelContext; }
}
