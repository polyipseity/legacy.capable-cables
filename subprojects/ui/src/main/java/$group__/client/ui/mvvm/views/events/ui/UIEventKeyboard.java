package $group__.client.ui.mvvm.views.events.ui;

import $group__.client.ui.events.ui.UIEvent;
import $group__.client.ui.mvvm.core.structures.IUIDataKeyboardKeyPress;
import $group__.client.ui.mvvm.core.views.events.IUIEventKeyboard;
import $group__.client.ui.mvvm.core.views.events.IUIEventTarget;
import net.minecraft.util.ResourceLocation;

public class UIEventKeyboard extends UIEvent implements IUIEventKeyboard {
	protected final IUIDataKeyboardKeyPress data;

	public UIEventKeyboard(ResourceLocation type, boolean canBubble, boolean cancelable, IUIEventTarget target, IUIDataKeyboardKeyPress data) {
		super(type, canBubble, cancelable, target);
		this.data = data;
	}

	@Override
	public IUIDataKeyboardKeyPress getData() { return data; }
}
