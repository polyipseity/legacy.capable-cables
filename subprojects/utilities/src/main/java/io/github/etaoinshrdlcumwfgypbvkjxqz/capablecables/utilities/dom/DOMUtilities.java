package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dom;

import com.google.common.collect.ImmutableList;
import org.w3c.dom.Node;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public enum DOMUtilities {
	;

	public static Optional<Node> getChildByTagName(Node node, CharSequence name) { return getChildrenByTagName(node, name).stream().unordered().findAny(); }

	@SuppressWarnings({"UnstableApiUsage", "CallToSuspiciousStringMethod"})
	public static List<Node> getChildrenByTagName(Node node, CharSequence name) {
		String name2 = name.toString();
		return new NodeListList(node.getChildNodes()).stream().sequential()
				.filter(n -> name2.equals(n.getLocalName()))
				.collect(ImmutableList.toImmutableList());
	}

	public static Optional<Node> getChildByTagNameNS(Node node, @Nullable CharSequence namespaceURI, CharSequence name) { return getChildrenByTagNameNS(node, namespaceURI, name).stream().unordered().findAny(); }

	@SuppressWarnings({"UnstableApiUsage", "CallToSuspiciousStringMethod"})
	public static List<Node> getChildrenByTagNameNS(Node node, @Nullable CharSequence namespaceURI, CharSequence name) {
		@Nullable String namespaceURI2 = Optional.ofNullable(namespaceURI).map(CharSequence::toString).orElse(null);
		String name2 = name.toString();
		return new NodeListList(node.getChildNodes()).stream().sequential()
				.filter(n -> Objects.equals(n.getNamespaceURI(), namespaceURI2)
						&& name2.equals(n.getLocalName()))
				.collect(ImmutableList.toImmutableList());
	}

	public static Optional<String> getAttributeValue(Node node, CharSequence name) {
		return getAttributeNode(node, name)
				.map(Node::getNodeValue);
	}

	public static Optional<Node> getAttributeNode(Node node, CharSequence name) {
		return Optional.ofNullable(node.getAttributes())
				.map(as -> as.getNamedItem(name.toString()));
	}

	public static Optional<String> getAttributeValueNS(Node node, @Nullable CharSequence namespaceURI, CharSequence name) {
		return getAttributeNodeNS(node, namespaceURI, name)
				.map(Node::getNodeValue);
	}

	public static Optional<Node> getAttributeNodeNS(Node node, @Nullable CharSequence namespaceURI, CharSequence name) {
		return Optional.ofNullable(node.getAttributes())
				.map(as -> as.getNamedItemNS(Optional.ofNullable(namespaceURI).map(CharSequence::toString).orElse(null), name.toString()));
	}
}
