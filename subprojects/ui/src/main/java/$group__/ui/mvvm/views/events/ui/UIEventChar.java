package $group__.ui.mvvm.views.events.ui;

import $group__.ui.core.mvvm.views.events.IUIEventChar;
import $group__.ui.core.mvvm.views.events.IUIEventTarget;
import $group__.ui.events.ui.UIEvent;
import $group__.utilities.interfaces.INamespacePrefixedString;

public class UIEventChar extends UIEvent implements IUIEventChar {
	protected final char codePoint;
	protected final int modifiers;

	public UIEventChar(INamespacePrefixedString type, boolean canBubble, boolean cancelable, IUIEventTarget target, char codePoint, int modifiers) {
		super(type, canBubble, cancelable, target);
		this.codePoint = codePoint;
		this.modifiers = modifiers;
	}

	@Override
	public char getCodePoint() { return codePoint; }

	@Override
	public int getModifiers() { return modifiers; }
}
