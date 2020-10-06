package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components;

public interface IUIComponentContextStackMutator {
	IUIComponentContextMutatorResult push(IUIComponentContextStack contextStack, IUIComponent element);

	IUIComponentContextMutatorResult pop(IUIComponentContextStack contextStack);
}
