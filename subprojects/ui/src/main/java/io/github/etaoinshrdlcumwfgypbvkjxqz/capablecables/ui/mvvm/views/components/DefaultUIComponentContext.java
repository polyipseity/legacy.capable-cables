package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContextMutator;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IAffineTransformStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.paths.IPath;

public class DefaultUIComponentContext
		implements IUIComponentContext {
	private final IUIViewComponent<?, ?> view;
	private final IUIComponentContextMutator mutator;
	private final IPath<IUIComponent> path;
	private final IAffineTransformStack transformStack;
	private final IUIViewContext viewContext;

	public DefaultUIComponentContext(IUIViewComponent<?, ?> view,
	                                 IUIComponentContextMutator mutator,
	                                 IPath<IUIComponent> path,
	                                 IAffineTransformStack transformStack,
	                                 IUIViewContext viewContext) {
		assert transformStack.getData().size() - path.size() == 1;
		this.view = view;
		this.mutator = mutator;
		this.path = path.copy();
		this.transformStack = transformStack.copy(); // COMMENT do not automatically pop
		this.viewContext = viewContext.copy();
	}

	@Override
	public IUIComponentContextMutator getMutator() { return mutator; }

	@Override
	public IUIViewComponent<?, ?> getView() { return view; }

	@Override
	public DefaultUIComponentContext copy() { return new DefaultUIComponentContext(getView(), getMutator(), getPath(), getTransformStack(), getViewContext()); }

	@Override
	public IPath<IUIComponent> getPath() { return path; }

	@Override
	public IAffineTransformStack getTransformStack() { return transformStack; }

	@Override
	public IUIViewContext getViewContext() { return viewContext; }
}
