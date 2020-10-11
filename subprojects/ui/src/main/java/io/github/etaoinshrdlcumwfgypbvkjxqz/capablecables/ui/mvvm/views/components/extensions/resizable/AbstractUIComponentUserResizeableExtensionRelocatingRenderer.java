package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.extensions.resizable;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.IUIComponentUserResizableExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.rendering.NullUIRenderer;

public abstract class AbstractUIComponentUserResizeableExtensionRelocatingRenderer
		extends NullUIRenderer<IUIComponentUserResizableExtension<?>>
		implements IUIComponentUserResizableExtension.IResizingRenderer {
	public AbstractUIComponentUserResizeableExtensionRelocatingRenderer(UIRendererConstructor.IArguments arguments) {
		super(arguments);
	}
}
