package $group__.ui.mvvm.views.events.ui;

import $group__.ui.core.mvvm.views.events.IUIEventMouseWheel;
import $group__.ui.core.mvvm.views.events.IUIEventTarget;
import $group__.ui.core.structures.IUIDataMouseButtonClick;
import $group__.utilities.interfaces.INamespacePrefixedString;

import javax.annotation.Nullable;

public class UIEventMouseWheel extends UIEventMouse implements IUIEventMouseWheel {
	protected final double delta;

	public UIEventMouseWheel(INamespacePrefixedString type, boolean canBubble, boolean cancelable, IUIEventTarget target, IUIDataMouseButtonClick data, @Nullable IUIEventTarget relatedTarget, double delta) {
		super(type, canBubble, cancelable, target, data, relatedTarget);
		this.delta = delta;
	}

	@Override
	public double getDelta() { return delta; }
}
