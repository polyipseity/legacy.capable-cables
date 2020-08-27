package $group__.ui.core.mvvm.views.paths;

import java.util.List;
import java.util.Optional;

public interface IUINode {
	List<? extends IUINode> getChildNodes();

	Optional<? extends IUINode> getParentNode();
}
