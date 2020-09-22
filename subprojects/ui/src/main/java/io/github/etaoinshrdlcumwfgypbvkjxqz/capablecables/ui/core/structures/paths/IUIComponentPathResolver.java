package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.paths;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IAffineTransformStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IUIComponentContext;

import java.awt.geom.Point2D;
import java.util.function.Consumer;

public interface IUIComponentPathResolver<T extends IUIComponent> {
	void resolvePath(IUIComponentContext context, Point2D point, boolean virtual);

	@SuppressWarnings("UnusedReturnValue")
	boolean addVirtualElement(T element,
	                          T virtualElement);

	@SuppressWarnings("UnusedReturnValue")
	boolean removeVirtualElement(T element,
	                             T virtualElement);

	@SuppressWarnings("UnusedReturnValue")
	boolean moveVirtualElementToTop(T element,
	                                T virtualElement);

	boolean addChildrenTransformer(T element, Consumer<? super IAffineTransformStack> transformer);

	boolean removeChildrenTransformer(T element, Consumer<? super IAffineTransformStack> transformer);
}
