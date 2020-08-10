package $group__.utilities.structures;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.AbstractList;

public class NodeListList extends AbstractList<Node> {
	protected final NodeList nodeList;

	protected NodeListList(NodeList nodeList) { this.nodeList = nodeList; }

	public static NodeListList wrap(NodeList nodeList) { return new NodeListList(nodeList); }

	@Override
	public Node get(int index) { return getNodeList().item(index); }

	protected NodeList getNodeList() { return nodeList; }

	@Override
	public int size() { return getNodeList().getLength(); }
}
