package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.ICopyable;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Optional;

public interface IUIComponentContext
		extends AutoCloseable, ICopyable {
	IUIViewComponent<?, ?> getView();

	@Immutable
	IUIComponentContextStackMutator getMutator();

	IUIComponentContextStack getStackRef();

	@Immutable
	IUIViewContext getViewContext();

	@Override
	void close();

	@Override
	IUIComponentContext copy();

	enum StaticHolder {
		;

		public static Shape createContextualShape(IUIComponentContext context, Shape shape) {
			return getCurrentTransform(context).createTransformedShape(shape);
		}

		public static AffineTransform getCurrentTransform(IUIComponentContext context) {
			return context.getStackRef().getTransformStackRef().element();
		}

		public static Optional<? extends IUIComponent> getCurrentComponent(IUIComponentContext context) {
			return context.getStackRef().getPathRef().getPathEnd();
		}
	}
}
