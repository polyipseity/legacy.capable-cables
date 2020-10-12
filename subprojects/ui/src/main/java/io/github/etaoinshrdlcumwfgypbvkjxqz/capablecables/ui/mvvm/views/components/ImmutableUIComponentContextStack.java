package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContextStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IAffineTransformStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.paths.IPath;

public final class ImmutableUIComponentContextStack
		implements IUIComponentContextStack {
	private final IPath<IUIComponent> path;
	private final IAffineTransformStack transformStack;

	public ImmutableUIComponentContextStack(IPath<IUIComponent> path, IAffineTransformStack transformStack) {
		assert transformStack.size() - path.size() == 1;
		this.path = path.copy();
		this.transformStack = transformStack.copy();
	}

	@Override
	public IPath<IUIComponent> getPathRef() { return getPath(); }

	protected IPath<IUIComponent> getPath() { return path; }

	@Override
	public IAffineTransformStack getTransformStackRef() { return getTransformStack(); }

	protected IAffineTransformStack getTransformStack() { return transformStack; }

	@Override
	public IUIComponentContextStack copy() { return new ImmutableUIComponentContextStack(getPath(), getTransformStack()); }

	@Override
	public void close() {
		int size = getPath().size();
		getPath().parentPath(size);
		IAffineTransformStack.StaticHolder.popNTimes(getTransformStack(), size);
	}
}
