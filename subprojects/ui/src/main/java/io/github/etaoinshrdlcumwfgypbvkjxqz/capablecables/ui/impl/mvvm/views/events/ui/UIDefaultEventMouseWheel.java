package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.ui;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.events.IUIEventMouseWheel;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.events.IUIEventTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.io.def.IMouseButtonClickData;

public class UIDefaultEventMouseWheel extends UIDefaultEventMouse implements IUIEventMouseWheel {
	private final double delta;

	public UIDefaultEventMouseWheel(IIdentifier type, boolean canBubble, boolean cancelable, IUIViewContext viewContext, IUIEventTarget target, IMouseButtonClickData data, @Nullable IUIEventTarget relatedTarget, double delta) {
		super(type, canBubble, cancelable, viewContext, target, data, relatedTarget);
		this.delta = delta;
	}

	@Override
	public double getDelta() { return delta; }
}
