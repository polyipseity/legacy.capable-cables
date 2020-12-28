package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;

public interface IUIComponentRenderer<C extends IUIComponent>
		extends IUIRenderer<C> {
	void render(IUIComponentContext context, EnumRenderStage stage);

	enum EnumRenderStage {
		PRE_CHILDREN,
		POST_CHILDREN,
	}
}
