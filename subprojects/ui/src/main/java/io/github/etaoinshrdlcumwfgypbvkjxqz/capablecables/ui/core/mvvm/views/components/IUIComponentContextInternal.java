package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.paths.IPath;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Deque;

public interface IUIComponentContextInternal
		extends IUIComponentContext {
	IPath<IUIComponent> getPathRef();

	Deque<AffineTransform> getTransformStackRef();

	Deque<Shape> getClipStackRef();

	Graphics2D getGraphicsRef();

	@Override
	IUIComponentContextInternal clone();
}
