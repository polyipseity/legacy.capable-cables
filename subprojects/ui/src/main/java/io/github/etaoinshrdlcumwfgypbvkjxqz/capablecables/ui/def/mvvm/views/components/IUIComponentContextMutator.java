package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components;

public interface IUIComponentContextMutator {
	IUIComponentContextMutatorResult push(IUIComponentContext context, IUIComponent element);

	IUIComponentContextMutatorResult pop(IUIComponentContext context);
}
