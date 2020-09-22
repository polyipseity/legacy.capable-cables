package $group__.utilities.dom;

import $group__.utilities.AbstractDelegatingObject;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.Nullable;

public class NamedNodeMapNodeList
		extends AbstractDelegatingObject<NamedNodeMap>
		implements NodeList {
	public NamedNodeMapNodeList(NamedNodeMap delegated) { super(delegated); }

	@Nullable
	@Override
	public Node item(int index) { return getDelegate().item(index); }

	@Override
	public int getLength() { return getDelegate().getLength(); }
}
