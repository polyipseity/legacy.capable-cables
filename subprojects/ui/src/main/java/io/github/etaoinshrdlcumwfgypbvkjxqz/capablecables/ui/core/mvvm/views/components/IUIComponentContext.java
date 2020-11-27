package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.ICloneable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.paths.IPath;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.MarkersTemplate;
import org.slf4j.Marker;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Deque;
import java.util.Optional;

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

	IUIViewComponent<?, ?> getView();

	IUIComponentContextMutator getMutator();

	@Immutable IUIViewContext getViewContext();

	@Immutable IPath<? extends IUIComponent> getPathView();

	@Immutable Deque<? extends AffineTransform> getTransformStackView();

	@Immutable Deque<? extends Shape> getClipStackView();

	Graphics2D createGraphics();

	@Override
	void close();

	enum StaticHolder {
		;

		private static final Marker CLASS_MARKER = MarkersTemplate.addReferences(UIMarkers.getInstance().getClassMarker(),
				UIMarkers.getInstance().getMarkerStructure());

		public static Marker getClassMarker() {
			return CLASS_MARKER;
		}
	}
}
