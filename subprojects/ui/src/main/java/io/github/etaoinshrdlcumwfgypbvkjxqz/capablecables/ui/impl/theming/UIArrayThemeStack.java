package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.theming;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.theming.IUITheme;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Consumer;

public class UIArrayThemeStack
		extends UIAbstractThemeStack {
	private final Deque<IUITheme> delegate;

	public UIArrayThemeStack(Consumer<@Nonnull ? super IUITheme> applier, int initialCapacity) {
		super(applier);
		this.delegate = new ArrayDeque<>(initialCapacity);
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	@Override
	protected Deque<IUITheme> delegate() {
		return delegate;
	}
}
