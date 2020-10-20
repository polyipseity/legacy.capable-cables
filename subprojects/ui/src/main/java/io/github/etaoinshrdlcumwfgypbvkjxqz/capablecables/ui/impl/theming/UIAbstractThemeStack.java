package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.theming;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.theming.IUITheme;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.theming.IUIThemeStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.AbstractObjectStack;

import java.util.function.Consumer;

public abstract class UIAbstractThemeStack
		extends AbstractObjectStack<IUITheme>
		implements IUIThemeStack {
	private final Consumer<? super IUITheme> applier;

	public UIAbstractThemeStack(Consumer<? super IUITheme> applier) {
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

	protected Consumer<? super IUITheme> getApplier() { return applier; }
}
