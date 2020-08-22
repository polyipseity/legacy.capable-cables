package $group__.client.ui.mvvm.core.views.components;

import $group__.client.ui.core.structures.shapes.IShapeDescriptor;
import $group__.client.ui.mvvm.core.structures.IAffineTransformStack;
import $group__.client.ui.mvvm.core.views.IUIReshapeExplicitly;
import $group__.client.ui.mvvm.core.views.components.paths.IUIComponentPathResolver;
import $group__.client.ui.mvvm.core.views.events.IUIEventTarget;

import javax.annotation.Nullable;
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

	Optional<IUIEventTarget> changeFocus(@Nullable IUIEventTarget currentFocus, boolean next);

	List<IUIComponent> getChildrenFlatView();

	@Override
	default void onIndexMove(int previous, int next)
			throws UnsupportedOperationException { throw new UnsupportedOperationException(); }

	@Override
	default void onParentChange(@Nullable IUIComponentContainer previous, @Nullable IUIComponentContainer next)
			throws UnsupportedOperationException { throw new UnsupportedOperationException(); }

	@Override
	default boolean isFocusable() { return true; }

	@Override
	IShapeDescriptor<S> getShapeDescriptor();
}
