package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components;

public interface IUIComponentContextMutator {
	IUIComponentContextMutatorResult push(IUIComponentContext context, IUIComponent element);

	IUIComponentContextMutatorResult pop(IUIComponentContext context);
}
