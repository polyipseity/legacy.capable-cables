package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.theming;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;

import java.util.Iterator;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IUITheme {
	void apply(Iterator<? extends IUIRendererContainer<?>> rendererContainers);
}
