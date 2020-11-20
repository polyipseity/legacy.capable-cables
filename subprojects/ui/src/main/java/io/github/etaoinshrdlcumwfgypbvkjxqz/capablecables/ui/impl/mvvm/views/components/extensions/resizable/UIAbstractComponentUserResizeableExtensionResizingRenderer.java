package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions.resizable;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.IUIRendererArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.IUIComponentUserResizableExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering.UIDefaultRenderer;

public abstract class UIAbstractComponentUserResizeableExtensionResizingRenderer
		extends UIDefaultRenderer<IUIComponentUserResizableExtension<?>>
		implements IUIComponentUserResizableExtension.IResizingRenderer {
	public UIAbstractComponentUserResizeableExtensionResizingRenderer(IUIRendererArguments arguments) {
		super(arguments);
	}
}
