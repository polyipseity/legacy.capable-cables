package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIComponentRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIRendererConstructor;

public class UIDefaultComponentRenderer<C extends IUIComponent>
		extends UIDefaultRenderer<C>
		implements IUIComponentRenderer<C> {
	@UIRendererConstructor
	public UIDefaultComponentRenderer(UIRendererConstructor.IArguments arguments) { super(arguments); }
}
