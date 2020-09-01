package $group__.utilities.dom;

import $group__.utilities.interfaces.IDelegating;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.AbstractList;

public class NodeListList
		extends AbstractList<Node>
		implements IDelegating<NodeList> {
	protected final NodeList delegated;

	public NodeListList(NodeList delegated) { this.delegated = delegated; }

	@Override
	public Node get(int index) { return getDelegated().item(index); }

	@Override
	public NodeList getDelegated() { return delegated; }

	@Override
	public int size() { return getDelegated().getLength(); }
}
