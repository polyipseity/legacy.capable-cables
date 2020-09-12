package $group__.ui.core.mvvm.views.components.paths;

import $group__.ui.core.mvvm.structures.IAffineTransformStack;
import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.paths.IPathResolver;

import java.awt.geom.Point2D;
import java.util.function.Consumer;

public interface IUIComponentPathResolver<T extends IUIComponent>
		extends IPathResolver<T> {
	@Override
	IUIComponentPath<T> resolvePath(Point2D point, boolean virtual);

	boolean addChildrenTransformer(T element, Consumer<? super IAffineTransformStack> transformer);

	boolean removeChildrenTransformer(T element, Consumer<? super IAffineTransformStack> transformer);
}
