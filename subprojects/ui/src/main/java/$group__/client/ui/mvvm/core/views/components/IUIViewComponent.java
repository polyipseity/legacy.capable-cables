package $group__.client.ui.mvvm.core.views.components;

import $group__.client.ui.mvvm.core.structures.IAffineTransformStack;
import $group__.client.ui.mvvm.core.structures.IShapeDescriptor;
import $group__.client.ui.mvvm.core.views.IUIView;

public interface IUIViewComponent<SD extends IShapeDescriptor<?>, M extends IUIComponentManager<?>>
		extends IUIView<SD> {
	M getManager();

	IAffineTransformStack getCleanTransformStack();
}
