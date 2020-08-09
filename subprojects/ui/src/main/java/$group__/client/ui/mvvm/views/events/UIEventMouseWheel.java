package $group__.client.ui.mvvm.views.events;

import $group__.client.ui.mvvm.core.structures.IUIDataMouseButtonClick;
import $group__.client.ui.mvvm.views.core.events.IUIEventMouseWheel;
import $group__.client.ui.mvvm.views.core.events.IUIEventTarget;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class UIEventMouseWheel extends UIEventMouse implements IUIEventMouseWheel {
	protected final double delta;

	public UIEventMouseWheel(ResourceLocation type, boolean canBubble, IUIEventTarget target, IUIDataMouseButtonClick data, @Nullable IUIEventTarget relatedTarget, double delta) {
		super(type, canBubble, target, data, relatedTarget);
		this.delta = delta;
	}

	@Override
	public double getDelta() { return delta; }
}
