package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions.resizable;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.IUIComponentUserResizableExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering.UIDefaultRenderer;

public abstract class UIAbstractComponentUserResizeableExtensionRelocatingRenderer
		extends UIDefaultRenderer<IUIComponentUserResizableExtension<?>>
		implements IUIComponentUserResizableExtension.IResizingRenderer {
	public UIAbstractComponentUserResizeableExtensionRelocatingRenderer(UIRendererConstructor.IArguments arguments) {
		super(arguments);
	}
}
