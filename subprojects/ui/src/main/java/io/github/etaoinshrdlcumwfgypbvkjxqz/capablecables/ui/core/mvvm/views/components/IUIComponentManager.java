package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIReshapeExplicitly;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptor;

import java.awt.*;
import java.util.Optional;

public interface IUIComponentManager<S extends Shape>
		extends IUIComponent, IUIReshapeExplicitly<S> {
	Optional<? extends IUIViewComponent<?, ?>> getView();

	void setView(@Nullable IUIViewComponent<?, ?> view);

	@Override
	IShapeDescriptor<S> getShapeDescriptor();
}
