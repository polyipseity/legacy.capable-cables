package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.IUIRendererArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIComponentRenderer;

public class UIDefaultComponentRenderer<C extends IUIComponent>
		extends UIDefaultRenderer<C>
		implements IUIComponentRenderer<C> {
	@UIRendererConstructor
	public UIDefaultComponentRenderer(IUIRendererArguments arguments) { super(arguments); }
}
