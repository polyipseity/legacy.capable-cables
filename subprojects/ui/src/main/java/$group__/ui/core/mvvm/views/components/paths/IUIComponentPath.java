package $group__.ui.core.mvvm.views.components.paths;

import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.paths.IPath;
import $group__.ui.core.structures.IHasAffineTransformStack;

public interface IUIComponentPath<T extends IUIComponent>
		extends IPath<T>, IHasAffineTransformStack {
	@Override
	default int sizeOfTransformStack() { return size(); }
}
