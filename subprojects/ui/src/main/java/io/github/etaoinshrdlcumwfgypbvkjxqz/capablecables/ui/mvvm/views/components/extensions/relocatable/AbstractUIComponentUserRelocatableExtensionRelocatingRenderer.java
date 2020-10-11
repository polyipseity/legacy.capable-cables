package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.extensions.relocatable;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.IUIComponentUserRelocatableExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.rendering.NullUIRenderer;

public abstract class AbstractUIComponentUserRelocatableExtensionRelocatingRenderer
		extends NullUIRenderer<IUIComponentUserRelocatableExtension<?>>
		implements IUIComponentUserRelocatableExtension.IRelocatingRenderer {
	public AbstractUIComponentUserRelocatableExtensionRelocatingRenderer(UIRendererConstructor.IArguments arguments) {
		super(arguments);
	}
}
