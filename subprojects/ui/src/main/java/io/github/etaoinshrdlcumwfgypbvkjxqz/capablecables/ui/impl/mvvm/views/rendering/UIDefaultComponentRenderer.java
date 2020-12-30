package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction.IUIRendererArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.rendering.IUIComponentRenderer;

public class UIDefaultComponentRenderer<C extends IUIComponent>
		extends UIDefaultRenderer<C>
		implements IUIComponentRenderer<C> {
	@UIRendererConstructor
	public UIDefaultComponentRenderer(IUIRendererArguments arguments) { super(arguments); }

	@Override
	public void render(IUIComponentContext context, EnumRenderStage stage) {}
}
