package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.traits.IHasBinding;
import org.jetbrains.annotations.NonNls;

public interface IUIRendererContainerContainer<R extends IUIRenderer<?>>
		extends IHasBinding {
	IUIRendererContainer<? extends R> getRendererContainer()
			throws IllegalStateException;

	void initializeRendererContainer(@NonNls CharSequence name)
			throws IllegalStateException;
}
