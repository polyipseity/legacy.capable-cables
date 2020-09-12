package $group__.ui.core.mvvm.views.components;

import $group__.ui.core.mvvm.structures.IAffineTransformStack;
import $group__.ui.core.mvvm.views.IUIView;

import java.awt.*;
import java.util.Optional;

public interface IUIViewComponent<S extends Shape, M extends IUIComponentManager<?>>
		extends IUIView<S> {
	Optional<? extends M> getManager();

	void setManager(M manager);

	IAffineTransformStack getCleanTransformStack();
}
