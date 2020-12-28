package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.theming;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewCoordinator;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;

import java.util.Deque;

public interface IUIThemeStack
		extends Deque<IUITheme>, IUIViewCoordinator {
	void applyAll(Iterable<? extends IUIRendererContainer<?>> rendererContainers);
}
