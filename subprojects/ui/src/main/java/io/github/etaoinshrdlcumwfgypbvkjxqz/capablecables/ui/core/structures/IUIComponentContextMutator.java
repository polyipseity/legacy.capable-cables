package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;

public interface IUIComponentContextMutator {
	IUIComponentContextMutatorResult push(IUIComponentContext context, IUIComponent element);

	IUIComponentContextMutatorResult pop(IUIComponentContext context);
}
