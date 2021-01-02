package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.theming;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.IUIViewCoordinator;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.rendering.IUIRendererContainer;

import java.util.Deque;
import java.util.Iterator;

public interface IUIThemeStack
		extends IUIViewCoordinator /* COMMENT do not extend 'Deque', bridge to 'Deque' provided by 'asDequeView' */ {
	void apply(Iterator<? extends IUIRendererContainer<?>> rendererContainers);

	void push(IUITheme theme);

	IUITheme pop();

	@SuppressWarnings("unused")
	@Immutable Deque<? extends IUITheme> asDequeView();
}
