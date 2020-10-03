package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IAffineTransformStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.ICopyable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.paths.IPath;

import java.awt.*;

public interface IUIComponentContext
		extends ICopyable, AutoCloseable {
	IUIViewComponent<?, ?> getView();

	IUIComponentContextMutator getMutator();

	@Override
	IUIComponentContext copy();

	@Override
	default void close() {
		IPath<IUIComponent> path = getPath();
		path.parentPath(path.size());
		getTransformStack().close();
	}

	IPath<IUIComponent> getPath();

	IAffineTransformStack getTransformStack();

	IUIViewContext getViewContext();

	enum StaticHolder {
		;

		public static Shape createContextualShape(IUIComponentContext context, Shape shape) {
			return context.getTransformStack().element().createTransformedShape(shape);
		}
	}
}
