package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions.resizable;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction.IUIRendererArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.extensions.IUIComponentUserResizableExtension;

public class UIComponentUserResizeableExtensionEmptyResizingRenderer
		extends UIAbstractComponentUserResizeableExtensionResizingRenderer {
	@UIRendererConstructor
	public UIComponentUserResizeableExtensionEmptyResizingRenderer(IUIRendererArguments arguments) {
		super(arguments);
	}

	@Override
	public void render(IUIComponentContext context, IUIComponentUserResizableExtension.IResizeData data) {}
}
