package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering;

public interface IUIRendererContainerContainer<R extends IUIRenderer<?>> {
	IUIRendererContainer<? extends R> getRendererContainer()
			throws IllegalStateException;

	void initializeRendererContainer(String name)
			throws IllegalStateException;
}
