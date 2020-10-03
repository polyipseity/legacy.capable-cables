package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.events.ui;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventChar;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.events.ui.UIEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.INamespacePrefixedString;

public class UIEventChar extends UIEvent implements IUIEventChar {
	protected final char codePoint;
	protected final int modifiers;

	public UIEventChar(INamespacePrefixedString type, boolean canBubble, boolean cancelable, IUIViewContext viewContext, IUIEventTarget target, char codePoint, int modifiers) {
		super(type, canBubble, cancelable, viewContext, target);
		this.codePoint = codePoint;
		this.modifiers = modifiers;
	}

	@Override
	public char getCodePoint() { return codePoint; }

	@Override
	public int getModifiers() { return modifiers; }
}
