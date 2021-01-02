package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.theming;

import com.google.common.collect.ForwardingDeque;
import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.theming.IUITheme;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.theming.IUIThemeStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.ListBackedDeque;

import java.util.Deque;
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
		Iterable<IUIRendererContainer<?>> rendererContainers1 = ImmutableList.copyOf(rendererContainers); // COMMENT will be iterated multiple times, copy iterator
		descendingIterator() // COMMENT from tail to head, tail is the lowest theme, head is the highest theme
				.forEachRemaining(theme -> theme.apply(rendererContainers1.iterator()));
	}

	@Override
	public @Immutable Deque<? extends IUITheme> asDequeView() {
		return ListBackedDeque.ofImmutable(delegate());
	}
}
