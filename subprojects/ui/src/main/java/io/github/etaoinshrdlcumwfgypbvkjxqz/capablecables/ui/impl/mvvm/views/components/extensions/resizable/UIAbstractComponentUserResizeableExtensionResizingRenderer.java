package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions.resizable;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction.IUIRendererArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.extensions.IUIComponentUserResizableExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering.UIDefaultRenderer;

public abstract class UIAbstractComponentUserResizeableExtensionResizingRenderer
		extends UIDefaultRenderer<IUIComponentUserResizableExtension<?>>
		implements IUIComponentUserResizableExtension.IResizingRenderer {
	public UIAbstractComponentUserResizeableExtensionResizingRenderer(IUIRendererArguments arguments) {
		super(arguments);
	}
}
