package $group__.ui.core.mvvm.views.components;

import $group__.ui.core.mvvm.structures.IAffineTransformStack;
import $group__.ui.core.mvvm.views.IUIReshapeExplicitly;
import $group__.ui.core.mvvm.views.components.paths.IUIComponentPathResolver;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface IUIComponentManager<S extends Shape>
		extends IUIComponent, IUIReshapeExplicitly<S> {

	IUIComponentPathResolver<IUIComponent> getPathResolver();

	IAffineTransformStack getCleanTransformStack();

	static Optional<IUIComponent> getComponentByID(IUIComponentManager<?> manager, String id) {
		for (IUIComponent c : manager.getChildrenFlatView()) {
			if (c.getID().filter(Predicate.isEqual(id)).isPresent())
				return Optional.of(c);
		}
		return Optional.empty();
	}

	List<IUIComponent> getChildrenFlatView();

	@Override
	IShapeDescriptor<S> getShapeDescriptor();

	IUIComponentShapeAnchorController getShapeAnchorController();
}
