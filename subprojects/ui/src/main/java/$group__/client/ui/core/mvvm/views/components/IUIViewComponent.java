package $group__.client.ui.core.mvvm.views.components;

import $group__.client.ui.core.mvvm.structures.IAffineTransformStack;
import $group__.client.ui.core.mvvm.views.IUIView;

import java.awt.*;

public interface IUIViewComponent<S extends Shape, M extends IUIComponentManager<?>>
		extends IUIView<S> {
	M getManager();

	IAffineTransformStack getCleanTransformStack();
}
