package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.events.ui;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventMouseWheel;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IUIDataMouseButtonClick;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.INamespacePrefixedString;

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
