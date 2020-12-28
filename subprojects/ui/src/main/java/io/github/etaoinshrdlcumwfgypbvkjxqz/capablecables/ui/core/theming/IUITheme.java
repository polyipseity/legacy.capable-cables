package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.theming;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IUITheme {
	void apply(Iterable<? extends IUIRendererContainer<?>> rendererContainers);
}
