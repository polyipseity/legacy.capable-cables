package $group__.ui.core.structures;

import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.utilities.interfaces.ICopyable;
import $group__.utilities.structures.paths.IPath;

import java.awt.geom.Point2D;

public interface IUIComponentContext
		extends ICopyable, AutoCloseable {
	Point2D getCursorPositionView();

	@Override
	IUIComponentContext copy();

	void push(IUIComponent element);

	IUIComponent pop();

	@Override
	default void close() {
		IPath<IUIComponent> path = getPath();
		path.parentPath(path.size() - 1);
		getTransformStack().close();
	}

	IPath<IUIComponent> getPath();

	IAffineTransformStack getTransformStack();
}
