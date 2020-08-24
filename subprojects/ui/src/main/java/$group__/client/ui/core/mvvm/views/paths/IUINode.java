package $group__.client.ui.core.mvvm.views.paths;

import java.util.List;
import java.util.Optional;

public interface IUINode {
	List<IUINode> getChildNodes();

	Optional<IUINode> getParentNode();
}
