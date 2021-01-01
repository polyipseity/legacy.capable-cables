package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.ui;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.events.IUIEventKeyboard;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.events.IUIEventTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui.UIDefaultEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.io.def.IKeyboardKeyPressData;

public class UIDefaultEventKeyboard extends UIDefaultEvent implements IUIEventKeyboard {
	private final IKeyboardKeyPressData data;

	public UIDefaultEventKeyboard(IIdentifier type, boolean canBubble, boolean cancelable, IUIViewContext viewContext, IUIEventTarget target, IKeyboardKeyPressData data) {
		super(type, canBubble, cancelable, viewContext, target);
		this.data = data;
	}

	@Override
	public IKeyboardKeyPressData getData() { return data; }
}
