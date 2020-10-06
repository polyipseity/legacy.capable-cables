package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IAffineTransformStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.ICopyable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.paths.IPath;

public interface IUIComponentContextStack
		extends ICopyable, AutoCloseable {
	IPath<IUIComponent> getPathRef();

	IAffineTransformStack getTransformStackRef();

	@Override
	IUIComponentContextStack copy();

	@Override
	void close();
}
