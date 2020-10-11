package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.extensions.resizable;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.IUIComponentUserResizableExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIRendererConstructor;

public class UIComponentUserResizeableExtensionNullRelocatingRenderer
		extends AbstractUIComponentUserResizeableExtensionRelocatingRenderer {
	@UIRendererConstructor
	public UIComponentUserResizeableExtensionNullRelocatingRenderer(UIRendererConstructor.IArguments arguments) {
		super(arguments);
	}

	@Override
	public void render(IUIComponentContext context, IUIComponentUserResizableExtension.IResizeData data) {}
}
