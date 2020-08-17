package $group__.client.ui.mvvm.views.events.ui;

import $group__.client.ui.events.ui.UIEvent;
import $group__.client.ui.mvvm.core.views.events.IUIEventChar;
import $group__.client.ui.mvvm.core.views.events.IUIEventTarget;
import net.minecraft.util.ResourceLocation;

public class UIEventChar extends UIEvent implements IUIEventChar {
	protected final char codePoint;
	protected final int modifiers;

	public UIEventChar(ResourceLocation type, boolean canBubble, boolean cancelable, IUIEventTarget target, char codePoint, int modifiers) {
		super(type, canBubble, cancelable, target);
		this.codePoint = codePoint;
		this.modifiers = modifiers;
	}

	@Override
	public char getCodePoint() { return codePoint; }

	@Override
	public int getModifiers() { return modifiers; }
}
