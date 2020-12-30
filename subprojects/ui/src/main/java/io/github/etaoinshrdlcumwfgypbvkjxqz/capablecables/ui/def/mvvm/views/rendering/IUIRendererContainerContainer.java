package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.rendering;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.traits.IHasBinding;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IUIRendererContainerContainer<R extends IUIRenderer<?>>
		extends IHasBinding {
	IUIRendererContainer<? extends R> getRendererContainer();
}
