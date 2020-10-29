package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.paths.IAffineTransformStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.paths.IPath;

import java.awt.*;

public interface IUIComponentContextInternal
		extends IUIComponentContext {
	IPath<IUIComponent> getPathRef();

	IAffineTransformStack getTransformStackRef();

	Graphics2D getGraphicsRef();

	@Override
	IUIComponentContextInternal clone();
}
