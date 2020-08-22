package $group__.client.ui.mvvm.core.views.components;

import $group__.client.ui.mvvm.core.structures.IAffineTransformStack;
import $group__.client.ui.mvvm.core.views.IUIView;

import java.awt.*;

public interface IUIViewComponent<S extends Shape, M extends IUIComponentManager<?>>
		extends IUIView<S> {
	M getManager();

	IAffineTransformStack getCleanTransformStack();
}
