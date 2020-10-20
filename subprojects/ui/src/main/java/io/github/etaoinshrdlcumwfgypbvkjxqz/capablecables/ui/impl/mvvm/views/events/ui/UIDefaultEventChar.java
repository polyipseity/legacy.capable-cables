package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.ui;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventChar;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui.UIDefaultEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;

public class UIDefaultEventChar extends UIDefaultEvent implements IUIEventChar {
	private final char codePoint;
	private final int modifiers;

	public UIDefaultEventChar(INamespacePrefixedString type, boolean canBubble, boolean cancelable, IUIViewContext viewContext, IUIEventTarget target, char codePoint, int modifiers) {
		super(type, canBubble, cancelable, viewContext, target);
		this.codePoint = codePoint;
		this.modifiers = modifiers;
	}

	@Override
	public char getCodePoint() { return codePoint; }

	@Override
	public int getModifiers() { return modifiers; }
}
