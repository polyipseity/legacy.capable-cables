package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContextInternal;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.paths.IAffineTransformStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.paths.IPath;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;

import java.awt.*;

public abstract class UIAbstractComponentContext
		implements IUIComponentContextInternal {
	@Override
	public UIAbstractComponentContext clone() {
		try {
			return (UIAbstractComponentContext) super.clone();
		} catch (CloneNotSupportedException e) {
			throw ThrowableUtilities.propagate(e);
		}
	}

	@Override
	public @Immutable IPath<IUIComponent> getPathView() {
		try {
			return getPathRef().clone();
		} catch (CloneNotSupportedException e) {
			throw ThrowableUtilities.propagate(e);
		}
	}

	@Override
	public @Immutable IAffineTransformStack getTransformStackView() {
		return getTransformStackRef().clone();
	}

	@Override
	public Graphics2D createGraphics() {
		return (Graphics2D) getGraphicsRef().create();
	}

	@Override
	public void close() {
		getGraphicsRef().dispose();
		int size = getPathRef().size();
		getPathRef().parentPath(size);
		IAffineTransformStack.popNTimes(getTransformStackRef(), size);
	}
}
