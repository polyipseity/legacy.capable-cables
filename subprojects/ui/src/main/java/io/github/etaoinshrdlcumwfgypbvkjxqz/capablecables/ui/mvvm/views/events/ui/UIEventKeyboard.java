package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.events.ui;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventKeyboard;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IUIDataKeyboardKeyPress;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.events.ui.UIEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;

public class UIEventKeyboard extends UIEvent implements IUIEventKeyboard {
	private final IUIDataKeyboardKeyPress data;

	public UIEventKeyboard(INamespacePrefixedString type, boolean canBubble, boolean cancelable, IUIViewContext viewContext, IUIEventTarget target, IUIDataKeyboardKeyPress data) {
		super(type, canBubble, cancelable, viewContext, target);
		this.data = data;
	}

	@Override
	public IUIDataKeyboardKeyPress getData() { return data; }
}
