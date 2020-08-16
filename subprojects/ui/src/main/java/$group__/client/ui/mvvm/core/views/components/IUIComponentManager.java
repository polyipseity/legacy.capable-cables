package $group__.client.ui.mvvm.core.views.components;

import $group__.client.ui.mvvm.core.structures.IAffineTransformStack;
import $group__.client.ui.mvvm.core.structures.IShapeDescriptor;
import $group__.client.ui.mvvm.core.views.IUIReshapeExplicitly;
import $group__.client.ui.mvvm.core.views.components.paths.IUIComponentPathResolver;
import $group__.client.ui.mvvm.core.views.events.IUIEventTarget;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public interface IUIComponentManager<SD extends IShapeDescriptor<?>>
		extends IUIComponent, IUIReshapeExplicitly<SD> {

	IUIComponentPathResolver<IUIComponent> getPathResolver();

	IAffineTransformStack getCleanTransformStack();

	List<IUIComponent> getChildrenFlat();

	Optional<IUIEventTarget> changeFocus(@Nullable IUIEventTarget currentFocus, boolean next);

	@Override
	SD getShapeDescriptor();

	@Override
	default void onIndexMove(int previous, int next)
			throws UnsupportedOperationException { throw new UnsupportedOperationException(); }

	@Override
	default void onParentChange(@Nullable IUIComponentContainer previous, @Nullable IUIComponentContainer next)
			throws UnsupportedOperationException { throw new UnsupportedOperationException(); }

	@Override
	default boolean isFocusable() { return true; }
}
