package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions.resizable;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.IUIComponentUserResizableExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIRendererConstructor;

public class UIComponentUserResizeableExtensionEmptyRelocatingRenderer
		extends UIAbstractComponentUserResizeableExtensionRelocatingRenderer {
	@UIRendererConstructor
	public UIComponentUserResizeableExtensionEmptyRelocatingRenderer(UIRendererConstructor.IArguments arguments) {
		super(arguments);
	}

	@Override
	public void render(IUIComponentContext context, IUIComponentUserResizableExtension.IResizeData data) {}
}
