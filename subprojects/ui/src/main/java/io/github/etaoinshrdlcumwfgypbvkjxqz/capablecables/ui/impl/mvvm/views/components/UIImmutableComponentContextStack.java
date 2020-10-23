package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContextStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.paths.IAffineTransformStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.DynamicUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.paths.IPath;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;

public final class UIImmutableComponentContextStack
		implements IUIComponentContextStack {
	private final IPath<IUIComponent> path;
	private final IAffineTransformStack transformStack;

	private static final long PATH_FIELD_OFFSET;

	@Override
	public IPath<IUIComponent> getPathRef() { return getPath(); }

	protected IPath<IUIComponent> getPath() { return path; }

	@Override
	public IAffineTransformStack getTransformStackRef() { return getTransformStack(); }

	protected IAffineTransformStack getTransformStack() { return transformStack; }

	@Override
	public void close() {
		int size = getPath().size();
		getPath().parentPath(size);
		IAffineTransformStack.popNTimes(getTransformStack(), size);
	}

	private static final long TRANSFORM_STACK_FIELD_OFFSET;

	static {
		try {
			PATH_FIELD_OFFSET = DynamicUtilities.getUnsafe().objectFieldOffset(UIImmutableComponentContextStack.class.getDeclaredField("path"));
			TRANSFORM_STACK_FIELD_OFFSET = DynamicUtilities.getUnsafe().objectFieldOffset(UIImmutableComponentContextStack.class.getDeclaredField("transformStack"));
		} catch (NoSuchFieldException e) {
			throw ThrowableUtilities.propagate(e);
		}
	}

	public UIImmutableComponentContextStack(IPath<IUIComponent> path, IAffineTransformStack transformStack) {
		assert transformStack.size() - path.size() == 1;
		try {
			this.path = path.clone();
			this.transformStack = transformStack.clone();
		} catch (CloneNotSupportedException e) {
			throw ThrowableUtilities.propagate(e);
		}
	}

	@SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
	@Override
	public final UIImmutableComponentContextStack clone() {
		UIImmutableComponentContextStack result;
		try {
			result = (UIImmutableComponentContextStack) super.clone();
			DynamicUtilities.getUnsafe().putObject(result, getPathFieldOffset(), result.path.clone());
			DynamicUtilities.getUnsafe().putObjectVolatile(result, getTransformStackFieldOffset(), result.transformStack.clone());
		} catch (CloneNotSupportedException e) {
			throw ThrowableUtilities.propagate(e);
		}
		return result;
	}

	protected static long getPathFieldOffset() {
		return PATH_FIELD_OFFSET;
	}

	protected static long getTransformStackFieldOffset() {
		return TRANSFORM_STACK_FIELD_OFFSET;
	}
}
