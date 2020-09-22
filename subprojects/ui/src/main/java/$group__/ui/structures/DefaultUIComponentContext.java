package $group__.ui.structures;

import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.IUIComponentContainer;
import $group__.ui.core.structures.IAffineTransformStack;
import $group__.ui.core.structures.IUIComponentContext;
import $group__.utilities.structures.paths.IPath;
import com.google.common.collect.ImmutableList;

import java.awt.geom.Point2D;

public class DefaultUIComponentContext
		implements IUIComponentContext {
	protected final IPath<IUIComponent> path;
	protected final IAffineTransformStack transformStack;
	protected final Point2D cursorPosition;

	public DefaultUIComponentContext(IPath<IUIComponent> path, IAffineTransformStack transformStack, Point2D cursorPosition) {
		this.path = path.copy();
		this.transformStack = transformStack.copy(); // COMMENT do not automatically pop
		this.cursorPosition = ImmutablePoint2D.copyOf(cursorPosition);
	}

	@Override
	public Point2D getCursorPositionView() { return getCursorPosition(); }

	protected Point2D getCursorPosition() { return cursorPosition; }

	@Override
	public DefaultUIComponentContext copy() { return new DefaultUIComponentContext(getPath(), getTransformStack(), getCursorPosition()); }

	@Override
	public void push(IUIComponent element) {
		IUIComponent pathEnd = getPath().getPathEnd();
		getTransformStack().push();
		if (pathEnd instanceof IUIComponentContainer)
			((IUIComponentContainer) pathEnd).transformChildren(getTransformStack());
		else
			throw new IllegalStateException();
		getPath().subPath(ImmutableList.of(element));
	}

	@Override
	public IUIComponent pop() {
		IUIComponent pathEnd = getPath().getPathEnd();
		getPath().parentPath(1);
		getTransformStack().pop();
		return pathEnd;
	}

	@Override
	public IPath<IUIComponent> getPath() { return path; }

	@Override
	public IAffineTransformStack getTransformStack() { return transformStack; }
}
