package $group__.ui.core.structures.paths;

import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.structures.IAffineTransformStack;
import $group__.ui.core.structures.IUIComponentContext;

import java.awt.geom.Point2D;
import java.util.function.Consumer;

public interface IUIComponentPathResolver<T extends IUIComponent> {
	void resolvePath(IUIComponentContext context, Point2D point, boolean virtual);

	@SuppressWarnings("UnusedReturnValue")
	boolean addVirtualElement(T element,
	                          T virtualElement);

	boolean removeVirtualElement(T element,
	                             T virtualElement);

	@SuppressWarnings("UnusedReturnValue")
	boolean moveVirtualElementToTop(T element,
	                                T virtualElement);

	boolean addChildrenTransformer(T element, Consumer<? super IAffineTransformStack> transformer);

	boolean removeChildrenTransformer(T element, Consumer<? super IAffineTransformStack> transformer);
}
