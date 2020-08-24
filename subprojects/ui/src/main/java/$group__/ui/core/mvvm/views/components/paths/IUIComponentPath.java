package $group__.ui.core.mvvm.views.components.paths;

import $group__.ui.core.mvvm.structures.IHasAffineTransformStack;
import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.paths.IUINodePath;

import java.util.List;

public interface IUIComponentPath
		extends IUINodePath, IHasAffineTransformStack {
	@Override
	default int sizeOfTransformStack() { return size(); }

	@Override
	default IUIComponent getPathRoot() { return getAt(0); }

	@Override
	IUIComponent getAt(int depth);

	@Override
	default IUIComponent getPathEnd() { return getAt(-1); }

	@Override
	List<? extends IUIComponent> asList();


}
