package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.events.ui;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventFocus;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.events.ui.UIEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;

import javax.annotation.Nullable;
import java.util.Optional;

public class UIEventFocus extends UIEvent implements IUIEventFocus {
	@Nullable
	private final IUIEventTarget relatedTarget;

	public UIEventFocus(INamespacePrefixedString type, boolean canBubble, boolean cancelable, IUIViewContext viewContext, IUIEventTarget target, @Nullable IUIEventTarget relatedTarget) {
		super(type, canBubble, cancelable, viewContext, target);
		this.relatedTarget = relatedTarget;
	}

	@Override
	public Optional<? extends IUIEventTarget> getRelatedTarget() { return Optional.ofNullable(relatedTarget); }
}
