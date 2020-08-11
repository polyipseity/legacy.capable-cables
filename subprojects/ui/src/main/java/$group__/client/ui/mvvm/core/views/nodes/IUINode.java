package $group__.client.ui.mvvm.core.views.nodes;

import java.util.List;
import java.util.Optional;

public interface IUINode {
	List<IUINode> getChildNodes();

	Optional<IUINode> getParentNode();
}
