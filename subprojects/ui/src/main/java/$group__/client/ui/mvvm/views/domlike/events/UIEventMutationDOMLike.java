package $group__.client.ui.mvvm.views.domlike.events;

import org.w3c.dom.Node;
import org.w3c.dom.events.MutationEvent;

import javax.annotation.Nullable;

public class UIEventMutationDOMLike extends UIEventDOMLike implements MutationEvent {
	@Nullable
	protected Node relatedNode;
	@Nullable
	protected String prevValue, newValue, attrName;
	protected short attrChangeArg;

	@Override
	@Nullable
	public Node getRelatedNode() { return relatedNode; }

	@Override
	@Nullable
	public String getPrevValue() { return prevValue; }

	@Override
	@Nullable
	public String getNewValue() { return newValue; }

	@Override
	@Nullable
	public String getAttrName() { return attrName; }

	@Override
	public short getAttrChange() { return attrChangeArg; }

	@Override
	public void initMutationEvent(String typeArg, boolean canBubbleArg, boolean cancelableArg, Node relatedNodeArg, String prevValueArg, String newValueArg, String attrNameArg, short attrChangeArg) {
		setRelatedNode(relatedNodeArg);
		setPrevValue(prevValueArg);
		setNewValue(newValueArg);
		setAttrName(attrNameArg);
		setAttrChangeArg(attrChangeArg);

		initEvent(typeArg, canBubbleArg, cancelableArg);
	}

	protected void setAttrChangeArg(short attrChangeArg) { this.attrChangeArg = attrChangeArg; }

	protected void setAttrName(String attrName) { this.attrName = attrName; }

	protected void setNewValue(String newValue) { this.newValue = newValue; }

	protected void setPrevValue(String prevValue) { this.prevValue = prevValue; }

	protected void setRelatedNode(Node relatedNode) { this.relatedNode = relatedNode; }
}
