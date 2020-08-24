package $group__.client.ui.mvvm.views.events.ui;

import $group__.client.ui.core.mvvm.views.events.IUIEventKeyboard;
import $group__.client.ui.core.mvvm.views.events.IUIEventTarget;
import $group__.client.ui.core.structures.IUIDataKeyboardKeyPress;
import $group__.client.ui.events.ui.UIEvent;
import $group__.utilities.interfaces.INamespacePrefixedString;

public class UIEventKeyboard extends UIEvent implements IUIEventKeyboard {
	protected final IUIDataKeyboardKeyPress data;

	public UIEventKeyboard(INamespacePrefixedString type, boolean canBubble, boolean cancelable, IUIEventTarget target, IUIDataKeyboardKeyPress data) {
		super(type, canBubble, cancelable, target);
		this.data = data;
	}

	@Override
	public IUIDataKeyboardKeyPress getData() { return data; }
}
