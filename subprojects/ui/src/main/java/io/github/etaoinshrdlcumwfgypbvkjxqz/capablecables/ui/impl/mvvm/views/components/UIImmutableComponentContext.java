package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContextStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContextStackMutator;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.DynamicUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;

public final class UIImmutableComponentContext
		implements IUIComponentContext {
	private final IUIViewContext viewContext;
	private final IUIViewComponent<?, ?> view;
	private final IUIComponentContextStackMutator mutator;
	private final IUIComponentContextStack stack;

	private static final long STACK_FIELD_OFFSET;

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

	static {
		try {
			STACK_FIELD_OFFSET = DynamicUtilities.getUnsafe().objectFieldOffset(UIImmutableComponentContext.class.getDeclaredField("stack"));
		} catch (NoSuchFieldException e) {
			throw ThrowableUtilities.propagate(e);
		}
	}

	public UIImmutableComponentContext(IUIViewContext viewContext,
	                                   IUIViewComponent<?, ?> view,
	                                   IUIComponentContextStackMutator mutator,
	                                   IUIComponentContextStack stack) {
		this.viewContext = viewContext;
		this.view = view;
		this.mutator = mutator;
		this.stack = stack.clone();
	}

	@SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
	@Override
	public UIImmutableComponentContext clone() {
		UIImmutableComponentContext result;
		try {
			result = (UIImmutableComponentContext) super.clone();
			DynamicUtilities.getUnsafe().putObjectVolatile(result, getStackFieldOffset(), result.stack.clone());
		} catch (CloneNotSupportedException e) {
			throw ThrowableUtilities.propagate(e);
		}
		return result;
	}

	protected static long getStackFieldOffset() {
		return STACK_FIELD_OFFSET;
	}
}
