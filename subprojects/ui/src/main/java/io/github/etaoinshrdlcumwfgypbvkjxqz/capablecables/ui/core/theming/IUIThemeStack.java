package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.theming;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewCoordinator;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;

import java.util.Deque;
import java.util.Iterator;

public interface IUIThemeStack
		extends Deque<IUITheme>, IUIViewCoordinator {
	void apply(Iterator<? extends IUIRendererContainer<?>> rendererContainers);
}
