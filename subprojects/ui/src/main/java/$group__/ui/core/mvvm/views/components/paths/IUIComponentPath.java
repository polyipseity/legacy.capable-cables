package $group__.ui.core.mvvm.views.components.paths;

import $group__.ui.core.mvvm.structures.IHasAffineTransformStack;
import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.paths.IPath;

public interface IUIComponentPath<T extends IUIComponent>
		extends IPath<T>, IHasAffineTransformStack {
	@Override
	default int sizeOfTransformStack() { return size(); }
}
