package $group__.client.ui.mvvm.core.views.components.paths;

import $group__.client.ui.mvvm.core.structures.IHasAffineTransformStack;
import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.client.ui.mvvm.core.views.paths.IUINodePath;

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
