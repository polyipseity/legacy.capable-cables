package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.*;

public abstract class UIAbstractComponentContextMutator
		implements IUIComponentContextMutator {
	@Override
	public final IUIComponentContextMutatorResult push(IUIComponentContext context, IUIComponent element) {
		return push((IUIComponentContextInternal) context, element);
	}

	@Override
	public final IUIComponentContextMutatorResult pop(IUIComponentContext context) {
		return pop((IUIComponentContextInternal) context);
	}

	protected abstract IUIComponentContextMutatorResult pop(IUIComponentContextInternal context);

	protected abstract IUIComponentContextMutatorResult push(IUIComponentContextInternal context, IUIComponent element);
}
