package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.paths.IAffineTransformStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.ICloneable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.paths.IPath;

public interface IUIComponentContextStack
		extends ICloneable, AutoCloseable {
	@Override
	IUIComponentContextStack clone();

	IPath<IUIComponent> getPathRef();

	IAffineTransformStack getTransformStackRef();

	@Override
	void close();
}
