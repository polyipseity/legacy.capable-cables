package $group__.client.ui.mvvm.views.domlike.nodes;

import java.util.List;
import java.util.Optional;

/**
 * @see org.w3c.dom.Node
 */
public interface IUIViewDOMLikeNode {
	List<IUIViewDOMLikeNode> getChildNodes();

	Optional<IUIViewDOMLikeNode> getParentNode();
}
