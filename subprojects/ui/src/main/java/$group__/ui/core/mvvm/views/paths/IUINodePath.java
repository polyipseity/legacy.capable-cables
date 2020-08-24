package $group__.ui.core.mvvm.views.paths;

import java.util.List;

public interface IUINodePath {
	int size();

	default IUINode getPathRoot() { return getAt(0); }

	IUINode getAt(int depth);

	default IUINode getPathEnd() { return getAt(-1); }

	List<? extends IUINode> asList();
}
