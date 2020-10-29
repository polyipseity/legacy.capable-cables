package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContextMutator;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.paths.IAffineTransformStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.DynamicUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.paths.IPath;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;

import javax.annotation.WillNotClose;
import java.awt.*;

public final class UIImmutableComponentContext
		extends UIAbstractComponentContext {
	private final IUIViewContext viewContext;
	private final IUIViewComponent<?, ?> view;
	private static final long PATH_FIELD_OFFSET;
	private static final long GRAPHICS_FIELD_OFFSET;
	private static final long TRANSFORM_STACK_FIELD_OFFSET;

	static {
		try {
			GRAPHICS_FIELD_OFFSET = DynamicUtilities.getUnsafe().objectFieldOffset(UIImmutableComponentContext.class.getDeclaredField("graphics"));
			PATH_FIELD_OFFSET = DynamicUtilities.getUnsafe().objectFieldOffset(UIImmutableComponentContext.class.getDeclaredField("path"));
			TRANSFORM_STACK_FIELD_OFFSET = DynamicUtilities.getUnsafe().objectFieldOffset(UIImmutableComponentContext.class.getDeclaredField("transformStack"));
		} catch (NoSuchFieldException e) {
			throw ThrowableUtilities.propagate(e);
		}
	}

	private final IUIComponentContextMutator mutator;
	private final Graphics2D graphics;
	private final IPath<IUIComponent> path;
	private final IAffineTransformStack transformStack;

	public UIImmutableComponentContext(IUIViewContext viewContext,
	                                   IUIViewComponent<?, ?> view,
	                                   IUIComponentContextMutator mutator,
	                                   @WillNotClose Graphics2D graphics,
	                                   IPath<IUIComponent> path,
	                                   IAffineTransformStack transformStack) {
		assert transformStack.size() - path.size() == 1;

		this.viewContext = viewContext;
		this.view = view;
		this.mutator = mutator;
		this.graphics = (Graphics2D) graphics.create();
		try {
			this.path = path.clone();
		} catch (CloneNotSupportedException e) {
			throw ThrowableUtilities.propagate(e);
		}
		this.transformStack = transformStack.clone();
	}

	@Override
	public IPath<IUIComponent> getPathRef() {
		return getPath();
	}

	protected IPath<IUIComponent> getPath() {
		return path;
	}

	@Override
	public IAffineTransformStack getTransformStackRef() {
		return getTransformStack();
	}

	protected IAffineTransformStack getTransformStack() {
		return transformStack;
	}

	@Override
	public Graphics2D getGraphicsRef() {
		return getGraphics();
	}

	protected Graphics2D getGraphics() {
		return graphics;
	}

	@SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
	@Override
	public UIImmutableComponentContext clone() {
		UIImmutableComponentContext result;
		try {
			result = (UIImmutableComponentContext) super.clone();
			DynamicUtilities.getUnsafe().putObject(result, getGraphicsFieldOffset(), result.graphics.create());
			DynamicUtilities.getUnsafe().putObject(result, getPathFieldOffset(), result.path.clone());
			DynamicUtilities.getUnsafe().putObjectVolatile(result, getTransformStackFieldOffset(), result.transformStack.clone());
		} catch (CloneNotSupportedException e) {
			throw ThrowableUtilities.propagate(e);
		}
		return result;
	}

	protected static long getGraphicsFieldOffset() {
		return GRAPHICS_FIELD_OFFSET;
	}

	protected static long getPathFieldOffset() {
		return PATH_FIELD_OFFSET;
	}

	protected static long getTransformStackFieldOffset() {
		return TRANSFORM_STACK_FIELD_OFFSET;
	}

	@Override
	public IUIViewComponent<?, ?> getView() {
		return view;
	}

	@Override
	public IUIComponentContextMutator getMutator() {
		return mutator;
	}

	@Override
	public @Immutable IUIViewContext getViewContext() {
		return viewContext;
	}
}
