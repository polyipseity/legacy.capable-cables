package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.ICopyable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.paths.EmptyPathException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.paths.IPath;

import java.awt.geom.Point2D;

public interface IUIComponentContext
		extends ICopyable, AutoCloseable {
	Point2D getCursorPositionView();

	@Override
	IUIComponentContext copy();

	void push(IUIComponent element);

	@SuppressWarnings("UnusedReturnValue")
	IUIComponent pop()
			throws EmptyPathException;

	@Override
	default void close() {
		IPath<IUIComponent> path = getPath();
		path.parentPath(path.size());
		getTransformStack().close();
	}

	IPath<IUIComponent> getPath();

	IAffineTransformStack getTransformStack();
}
