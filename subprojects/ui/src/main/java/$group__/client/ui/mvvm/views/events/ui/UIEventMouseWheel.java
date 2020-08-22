package $group__.client.ui.mvvm.views.events.ui;

import $group__.client.ui.core.structures.IUIDataMouseButtonClick;
import $group__.client.ui.mvvm.core.views.events.IUIEventMouseWheel;
import $group__.client.ui.mvvm.core.views.events.IUIEventTarget;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class UIEventMouseWheel extends UIEventMouse implements IUIEventMouseWheel {
	protected final double delta;

	public UIEventMouseWheel(ResourceLocation type, boolean canBubble, boolean cancelable, IUIEventTarget target, IUIDataMouseButtonClick data, @Nullable IUIEventTarget relatedTarget, double delta) {
		super(type, canBubble, cancelable, target, data, relatedTarget);
		this.delta = delta;
	}

	@Override
	public double getDelta() { return delta; }
}
