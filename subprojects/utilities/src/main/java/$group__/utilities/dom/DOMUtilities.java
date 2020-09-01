package $group__.utilities.dom;

import com.google.common.collect.ImmutableList;
import org.w3c.dom.Node;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public enum DOMUtilities {
	;

	public static Optional<Node> getChildByTagName(Node node, String name) { return getChildrenByTagName(node, name).stream().unordered().findAny(); }

	@SuppressWarnings("UnstableApiUsage")
	public static List<Node> getChildrenByTagName(Node node, String name) {
		return new NodeListList(node.getChildNodes()).stream().sequential()
				.filter(n -> name.equals(n.getLocalName()))
				.collect(ImmutableList.toImmutableList());
	}

	public static Optional<Node> getChildByTagNameNS(Node node, @Nullable String namespaceURI, String name) { return getChildrenByTagNameNS(node, namespaceURI, name).stream().unordered().findAny(); }

	@SuppressWarnings("UnstableApiUsage")
	public static List<Node> getChildrenByTagNameNS(Node node, @Nullable String namespaceURI, String name) {
		return new NodeListList(node.getChildNodes()).stream().sequential()
				.filter(n -> Objects.equals(n.getNamespaceURI(), namespaceURI)
						&& name.equals(n.getLocalName()))
				.collect(ImmutableList.toImmutableList());
	}

	public static Optional<String> getAttributeValue(Node node, String name) {
		return getAttributeNode(node, name)
				.map(Node::getNodeValue);
	}

	public static Optional<Node> getAttributeNode(Node node, String name) {
		return Optional.ofNullable(node.getAttributes())
				.map(as -> as.getNamedItem(name));
	}

	public static Optional<String> getAttributeValueNS(Node node, @Nullable String namespaceURI, String name) {
		return getAttributeNodeNS(node, namespaceURI, name)
				.map(Node::getNodeValue);
	}

	public static Optional<Node> getAttributeNodeNS(Node node, @Nullable String namespaceURI, String name) {
		return Optional.ofNullable(node.getAttributes())
				.map(as -> as.getNamedItemNS(namespaceURI, name));
	}
}
