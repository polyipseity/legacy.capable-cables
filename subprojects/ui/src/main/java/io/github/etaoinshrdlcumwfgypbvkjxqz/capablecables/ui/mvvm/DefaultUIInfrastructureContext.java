package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUIInfrastructureContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.viewmodels.IUIViewModelContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;

public class DefaultUIInfrastructureContext
		implements IUIInfrastructureContext {
	private final IUIViewContext viewContext;
	private final IUIViewModelContext viewModelContext;

	public DefaultUIInfrastructureContext(IUIViewContext viewContext, IUIViewModelContext viewModelContext) {
		this.viewContext = viewContext.copy();
		this.viewModelContext = viewModelContext.copy();
	}

	@Override
	public IUIViewContext getViewContext() { return viewContext; }

	@Override
	public IUIViewModelContext getViewModelContext() { return viewModelContext; }

	@Override
	public DefaultUIInfrastructureContext copy() { return new DefaultUIInfrastructureContext(getViewContext(), getViewModelContext()); }
}
