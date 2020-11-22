package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.theming;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.theming.IUITheme;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.theming.IUIThemeStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.AbstractObjectStack;

import java.util.function.Consumer;

public abstract class UIAbstractThemeStack
		extends AbstractObjectStack<IUITheme>
		implements IUIThemeStack {
	private final Consumer<@Nonnull ? super IUITheme> applier;

	public UIAbstractThemeStack(Consumer<@Nonnull ? super IUITheme> applier) {
		this.applier = applier;
	}

	@Override
	public IUITheme push(IUITheme element) {
		IUITheme ret = super.push(element);
		getApplier().accept(element());
		return ret;
	}

	@Override
	public IUITheme pop() {
		IUITheme ret = super.pop();
		getApplier().accept(element());
		return ret;
	}

	@Override
	public void applyAll(Iterable<? extends IUIRendererContainer<?>> rendererContainers) {
		getData().descendingIterator() // COMMENT from tail to head, tail is the lowest theme, head is the highest theme
				.forEachRemaining(theme -> theme.apply(rendererContainers));
	}

	protected Consumer<@Nonnull ? super IUITheme> getApplier() { return applier; }
}
