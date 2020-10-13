package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.traits.IHasBinding;

public interface IUIRendererContainerContainer<R extends IUIRenderer<?>>
		extends IHasBinding {
	IUIRendererContainer<? extends R> getRendererContainer()
			throws IllegalStateException;

	void initializeRendererContainer(String name)
			throws IllegalStateException;
}
