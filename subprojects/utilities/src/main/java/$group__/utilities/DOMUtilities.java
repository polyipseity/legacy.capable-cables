package $group__.utilities;

import $group__.utilities.structures.NodeListList;
import com.google.common.collect.ImmutableList;
import org.w3c.dom.Node;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

public enum DOMUtilities {
	;

	@SuppressWarnings("UnstableApiUsage")
	public static ImmutableList<Node> getChildrenByTagName(Node node, String name) {
		return NodeListList.wrap(node.getChildNodes()).stream().sequential()
				.filter(n -> name.equals(n.getLocalName()))
				.collect(ImmutableList.toImmutableList());
	}

	@SuppressWarnings("UnstableApiUsage")
	public static ImmutableList<Node> getChildrenByTagNameNS(Node node, @Nullable String namespaceURI, String name) {
		return NodeListList.wrap(node.getChildNodes()).stream().sequential()
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
