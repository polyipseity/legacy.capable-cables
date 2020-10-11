package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.rendering;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIComponentRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIRendererConstructor;

public class NullUIComponentRenderer<C extends IUIComponent>
		extends NullUIRenderer<C>
		implements IUIComponentRenderer<C> {
	@UIRendererConstructor
	public NullUIComponentRenderer(UIRendererConstructor.IArguments arguments) { super(arguments); }
}
