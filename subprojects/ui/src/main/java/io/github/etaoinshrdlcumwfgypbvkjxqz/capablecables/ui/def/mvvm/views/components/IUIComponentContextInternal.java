package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.paths.IPath;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Deque;
import java.util.Optional;

public interface IUIComponentContextInternal
		extends IUIComponentContext {
	IPath<IUIComponent> getPathRef();

	Deque<AffineTransform> getTransformStackRef();

	Deque<Shape> getClipStackRef();

	Optional<? extends Graphics2D> getGraphicsRef();

	@Override
	IUIComponentContextInternal clone();
}
