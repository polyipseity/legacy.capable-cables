package $group__.client.ui.mvvm.core.views.components.paths;

import $group__.client.ui.mvvm.core.structures.IAffineTransformStack;
import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.client.ui.mvvm.core.views.paths.IUINodePathResolver;

import java.awt.geom.Point2D;
import java.util.function.Consumer;

public interface IUIComponentPathResolver<T extends IUIComponent>
		extends IUINodePathResolver<T> {
	@Override
	IUIComponentPath resolvePath(Point2D point, boolean virtual);

	boolean addChildrenTransformer(T element, Consumer<? super IAffineTransformStack> transformer);

	boolean removeChildrenTransformer(T element, Consumer<? super IAffineTransformStack> transformer);
}
