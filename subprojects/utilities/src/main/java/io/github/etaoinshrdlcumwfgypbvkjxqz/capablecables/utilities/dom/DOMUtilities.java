package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dom;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import org.jetbrains.annotations.NonNls;
import org.w3c.dom.Node;

import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.stream.StreamSupport;

public enum DOMUtilities {
	;

	public static Optional<Node> getChildByTagName(Node node, CharSequence name) {
		return StreamSupport.stream(getChildrenByTagName(node, name), false).unordered()
				.findAny();
	}

	public static Spliterator<Node> getChildrenByTagName(Node node, CharSequence name) {
		@NonNls String name2 = name.toString();
		return new NodeListList(node.getChildNodes()).stream()
				.filter(n -> name2.equals(n.getLocalName()))
				.spliterator();
	}

	public static Optional<Node> getChildByTagNameNS(Node node, @Nullable CharSequence namespaceURI, CharSequence name) {
		return StreamSupport.stream(getChildrenByTagNameNS(node, namespaceURI, name), false).unordered()
				.findAny();
	}

	public static Spliterator<Node> getChildrenByTagNameNS(Node node, @Nullable CharSequence namespaceURI, CharSequence name) {
		@Nullable String namespaceURI2 = Optional.ofNullable(namespaceURI).map(CharSequence::toString).orElse(null);
		@NonNls String name2 = name.toString();

		return new NodeListList(node.getChildNodes()).stream()
				.filter(n -> Objects.equals(n.getNamespaceURI(), namespaceURI2)
						&& name2.equals(n.getLocalName()))
				.spliterator();
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
