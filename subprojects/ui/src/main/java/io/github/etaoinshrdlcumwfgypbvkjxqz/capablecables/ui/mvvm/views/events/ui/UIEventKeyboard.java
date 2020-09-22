package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.events.ui;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventKeyboard;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IUIDataKeyboardKeyPress;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.events.ui.UIEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.INamespacePrefixedString;

public class UIEventKeyboard extends UIEvent implements IUIEventKeyboard {
	protected final IUIDataKeyboardKeyPress data;

	public UIEventKeyboard(INamespacePrefixedString type, boolean canBubble, boolean cancelable, IUIEventTarget target, IUIDataKeyboardKeyPress data) {
		super(type, canBubble, cancelable, target);
		this.data = data;
	}

	@Override
	public IUIDataKeyboardKeyPress getData() { return data; }
}
