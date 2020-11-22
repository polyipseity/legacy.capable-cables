package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.theming;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewCoordinator;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IObjectStack;

public interface IUIThemeStack
		extends IObjectStack<IUITheme>, IUIViewCoordinator {
	void applyAll(Iterable<? extends IUIRendererContainer<?>> rendererContainers);
}
