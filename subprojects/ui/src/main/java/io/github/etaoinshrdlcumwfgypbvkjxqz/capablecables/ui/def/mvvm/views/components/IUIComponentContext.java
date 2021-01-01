package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics.AutoCloseableGraphics2D;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.ICloneable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.paths.IPath;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.MarkersTemplate;
import org.slf4j.Marker;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Deque;
import java.util.Optional;
import java.util.function.Consumer;

public interface IUIComponentContext
		extends ICloneable, AutoCloseable {
	static Shape createContextualShape(IUIComponentContext context, Shape shape) {
		return getCurrentTransform(context).createTransformedShape(shape);
	}

	static AffineTransform getCurrentTransform(IUIComponentContext context) {
		return ((IUIComponentContextInternal) context).getTransformStackRef().element();
	}

	static Optional<? extends IUIComponent> getCurrentComponent(IUIComponentContext context) {
		return ((IUIComponentContextInternal) context).getPathRef().getPathEnd();
	}

	@Override
	IUIComponentContext clone();

	IUIComponentView<?, ?> getView();

	IUIComponentContextMutator getMutator();

	@Immutable IUIViewContext getViewContext();

	@Immutable IPath<? extends IUIComponent> getPathView();

	@Immutable Deque<? extends AffineTransform> getTransformStackView();

	@Immutable Deque<? extends Shape> getClipStackView();

	static void withGraphics(IUIComponentContext instance, Consumer<@Nonnull ? super Graphics2D> action) {
		createCloseableGraphics(instance)
				.ifPresent(graphics -> {
					try (AutoCloseableGraphics2D graphics1 = graphics) {
						action.accept(graphics1);
					}
				});
	}

	@Override
	void close();

	static Optional<AutoCloseableGraphics2D> createCloseableGraphics(IUIComponentContext instance) {
		return instance.createGraphics()
				.map(AutoCloseableGraphics2D::of);
	}

	Optional<? extends Graphics2D> createGraphics();

	enum StaticHolder {
		;

		private static final Marker CLASS_MARKER = MarkersTemplate.addReferences(UIMarkers.getInstance().getClassMarker(),
				UIMarkers.getInstance().getMarkerStructure());

		public static Marker getClassMarker() {
			return CLASS_MARKER;
		}
	}
}
