package $group__.utilities;

import $group__.utilities.structures.NodeListList;
import com.google.common.collect.ImmutableList;
import org.w3c.dom.Node;

import javax.annotation.Nullable;
import java.util.Objects;

public enum DOMUtilities {
	;

	public static ImmutableList<Node> getChildrenByTagName(Node node, String name) {return getChildrenByTagNameNS(node, null, name);}

	public static ImmutableList<Node> getChildrenByTagNameNS(Node node, @Nullable String namespaceURI, String name) {
		return NodeListList.wrap(node.getChildNodes()).stream().filter(n -> Objects.equals(n.getNamespaceURI(), namespaceURI) && name.equals(n.getLocalName())).collect(ImmutableList.toImmutableList());
	}
}
