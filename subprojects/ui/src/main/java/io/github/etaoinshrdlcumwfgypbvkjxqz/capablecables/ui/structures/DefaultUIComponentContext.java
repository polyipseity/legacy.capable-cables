package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IAffineTransformStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.ImmutablePoint2D;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.paths.EmptyPathException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.paths.IPath;

import java.awt.geom.Point2D;
import java.util.Optional;

public class DefaultUIComponentContext
		implements IUIComponentContext {
	protected final IPath<IUIComponent> path;
	protected final IAffineTransformStack transformStack;
	protected final Point2D cursorPosition;

	public DefaultUIComponentContext(IPath<IUIComponent> path, IAffineTransformStack transformStack, Point2D cursorPosition) {
		assert transformStack.getData().size() - path.size() == 1;
		this.path = path.copy();
		this.transformStack = transformStack.copy(); // COMMENT do not automatically pop
		this.cursorPosition = ImmutablePoint2D.of(cursorPosition);
	}

	@Override
	public Point2D getCursorPositionView() { return getCursorPosition(); }

	protected Point2D getCursorPosition() { return cursorPosition; }

	@Override
	public DefaultUIComponentContext copy() { return new DefaultUIComponentContext(getPath(), getTransformStack(), getCursorPosition()); }

	@Override
	public void push(IUIComponent element) {
		Optional<? extends IUIComponent> pathEnd = getPath().getPathEnd();
		getTransformStack().push();
		pathEnd.ifPresent(pathEnd2 -> {
			if (pathEnd2 instanceof IUIComponentContainer)
				((IUIComponentContainer) pathEnd2).transformChildren(getTransformStack());
			else
				throw new IllegalStateException();
		});
		getPath().subPath(ImmutableList.of(element));
	}

	@Override
	public IUIComponent pop()
			throws EmptyPathException {
		Optional<? extends IUIComponent> pathEnd = getPath().getPathEnd();
		getPath().parentPath(1);
		assert pathEnd.isPresent();
		getTransformStack().pop();
		return pathEnd.get();
	}

	@Override
	public IPath<IUIComponent> getPath() { return path; }

	@Override
	public IAffineTransformStack getTransformStack() { return transformStack; }
}
