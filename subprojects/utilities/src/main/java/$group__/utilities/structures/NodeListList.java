package $group__.utilities.structures;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.AbstractList;

public class NodeListList extends AbstractList<Node> {
	protected final NodeList delegated;

	protected NodeListList(NodeList delegated) { this.delegated = delegated; }

	public static NodeListList wrap(NodeList nodeList) { return new NodeListList(nodeList); }

	@Override
	public Node get(int index) { return getDelegated().item(index); }

	protected NodeList getDelegated() { return delegated; }

	@Override
	public int size() { return getDelegated().getLength(); }
}
