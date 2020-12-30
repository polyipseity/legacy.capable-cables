package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.theming;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.rendering.IUIRendererContainer;

import java.util.Iterator;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IUITheme {
	void apply(Iterator<? extends IUIRendererContainer<?>> rendererContainers);
}
