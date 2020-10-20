package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContextStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContextStackMutator;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;

public final class UIImmutableComponentContext
		implements IUIComponentContext {
	private final IUIViewContext viewContext;
	private final IUIViewComponent<?, ?> view;
	private final IUIComponentContextStackMutator mutator;
	private final IUIComponentContextStack stack;

	public UIImmutableComponentContext(IUIViewContext viewContext,
	                                   IUIViewComponent<?, ?> view,
	                                   IUIComponentContextStackMutator mutator,
	                                   IUIComponentContextStack stack) {
		this.viewContext = viewContext;
		this.view = view;
		this.mutator = mutator;
		this.stack = stack.copy();
	}

	@Override
	public IUIViewComponent<?, ?> getView() { return view; }

	@Override
	public @Immutable IUIComponentContextStackMutator getMutator() { return mutator; }

	@Override
	public IUIComponentContextStack getStackRef() { return getStack(); }

	protected IUIComponentContextStack getStack() { return stack; }

	@Override
	public @Immutable IUIViewContext getViewContext() { return viewContext; }

	@Override
	public void close() { getStackRef().close(); }

	@Override
	public UIImmutableComponentContext copy() { return new UIImmutableComponentContext(getViewContext(), getView(), getMutator(), getStack()); }
}
