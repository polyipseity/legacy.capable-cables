package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.ui;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventKeyboard;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui.UIDefaultEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.IKeyboardKeyPressData;

public class UIDefaultEventKeyboard extends UIDefaultEvent implements IUIEventKeyboard {
	private final IKeyboardKeyPressData data;

	public UIDefaultEventKeyboard(INamespacePrefixedString type, boolean canBubble, boolean cancelable, IUIViewContext viewContext, IUIEventTarget target, IKeyboardKeyPressData data) {
		super(type, canBubble, cancelable, viewContext, target);
		this.data = data;
	}

	@Override
	public IKeyboardKeyPressData getData() { return data; }
}
