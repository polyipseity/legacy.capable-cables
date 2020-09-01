package $group__.utilities.dom;

import $group__.utilities.interfaces.IDelegating;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.Nullable;

public class NamedNodeMapNodeList
		extends IDelegating.Impl<NamedNodeMap>
		implements NodeList {
	public NamedNodeMapNodeList(NamedNodeMap delegated) { super(delegated); }

	@Nullable
	@Override
	public Node item(int index) { return getDelegated().item(index); }

	@Override
	public int getLength() { return getDelegated().getLength(); }
}
