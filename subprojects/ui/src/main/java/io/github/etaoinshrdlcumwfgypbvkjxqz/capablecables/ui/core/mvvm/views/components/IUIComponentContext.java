package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.paths.IAffineTransformStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.ICloneable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.paths.IPath;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Optional;

public interface IUIComponentContext
		extends ICloneable, AutoCloseable {
	@Override
	IUIComponentContext clone();

	static Shape createContextualShape(IUIComponentContext context, Shape shape) {
		return getCurrentTransform(context).createTransformedShape(shape);
	}

	static AffineTransform getCurrentTransform(IUIComponentContext context) {
		return ((IUIComponentContextInternal) context).getTransformStackRef().element();
	}

	static Optional<? extends IUIComponent> getCurrentComponent(IUIComponentContext context) {
		return ((IUIComponentContextInternal) context).getPathRef().getPathEnd();
	}

	IUIViewComponent<?, ?> getView();

	IUIComponentContextMutator getMutator();

	@Immutable IUIViewContext getViewContext();

	@Immutable IPath<IUIComponent> getPathView();

	@Immutable IAffineTransformStack getTransformStackView();

	Graphics2D createGraphics();

	@Override
	void close();
}
