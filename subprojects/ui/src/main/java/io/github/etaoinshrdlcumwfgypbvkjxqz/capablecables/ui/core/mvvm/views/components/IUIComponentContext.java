package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.ICloneable;

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
		return context.getStackRef().getTransformStackRef().element();
	}

	static Optional<? extends IUIComponent> getCurrentComponent(IUIComponentContext context) {
		return context.getStackRef().getPathRef().getPathEnd();
	}

	IUIViewComponent<?, ?> getView();

	@Immutable
	IUIComponentContextStackMutator getMutator();

	IUIComponentContextStack getStackRef();

	@Immutable
	IUIViewContext getViewContext();

	@Override
	void close();
}
