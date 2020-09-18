package $group__.utilities.structures.paths;

import java.util.List;
import java.util.Optional;

public interface INode {
	List<? extends INode> getChildNodes();

	Optional<? extends INode> getParentNode();
}
