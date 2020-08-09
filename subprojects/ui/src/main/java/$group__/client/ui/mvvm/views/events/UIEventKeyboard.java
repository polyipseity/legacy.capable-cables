package $group__.client.ui.mvvm.views.events;

import $group__.client.ui.mvvm.core.structures.IUIDataKeyboardKeyPress;
import $group__.client.ui.mvvm.views.core.events.IUIEventKeyboard;
import $group__.client.ui.mvvm.views.core.events.IUIEventTarget;
import net.minecraft.util.ResourceLocation;

public class UIEventKeyboard extends UIEvent implements IUIEventKeyboard {
	protected final IUIDataKeyboardKeyPress data;

	public UIEventKeyboard(ResourceLocation type, boolean canBubble, IUIEventTarget target, IUIDataKeyboardKeyPress data) {
		super(type, canBubble, target);
		this.data = data;
	}

	@Override
	public IUIDataKeyboardKeyPress getData() { return data; }
}
