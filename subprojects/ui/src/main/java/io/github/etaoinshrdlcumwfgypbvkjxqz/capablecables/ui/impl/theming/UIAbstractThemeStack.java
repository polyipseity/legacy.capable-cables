package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.theming;

import com.google.common.collect.ForwardingDeque;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.theming.IUITheme;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.theming.IUIThemeStack;

import java.util.Iterator;
import java.util.function.Consumer;

public abstract class UIAbstractThemeStack
		extends ForwardingDeque<IUITheme>
		implements IUIThemeStack {
	private final Consumer<@Nonnull ? super IUITheme> applier;

	public UIAbstractThemeStack(Consumer<@Nonnull ? super IUITheme> applier) {
		this.applier = applier;
	}

	@Override
	public IUITheme pop() {
		IUITheme popped = super.pop();
		getApplier().accept(element());
		return popped;
	}

	@Override
	public void push(IUITheme element) {
		super.push(element);
		getApplier().accept(element());
	}

	protected Consumer<@Nonnull ? super IUITheme> getApplier() { return applier; }

	@Override
	public void apply(Iterator<? extends IUIRendererContainer<?>> rendererContainers) {
		descendingIterator() // COMMENT from tail to head, tail is the lowest theme, head is the highest theme
				.forEachRemaining(theme -> theme.apply(rendererContainers));
	}
}
