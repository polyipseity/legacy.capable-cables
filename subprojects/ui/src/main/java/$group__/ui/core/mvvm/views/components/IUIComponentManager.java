package $group__.ui.core.mvvm.views.components;

import $group__.ui.core.mvvm.views.IUIReshapeExplicitly;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Optional;

public interface IUIComponentManager<S extends Shape>
		extends IUIComponent, IUIReshapeExplicitly<S> {
	Optional<? extends IUIViewComponent<?, ?>> getView();

	void setView(@Nullable IUIViewComponent<?, ?> view);

	@Override
	IShapeDescriptor<S> getShapeDescriptor();
}
