package $group__.ui.mvvm.views.events.ui;

import $group__.ui.core.mvvm.views.events.IUIEventFocus;
import $group__.ui.core.mvvm.views.events.IUIEventTarget;
import $group__.ui.events.ui.UIEvent;
import $group__.utilities.interfaces.INamespacePrefixedString;

import javax.annotation.Nullable;
import java.util.Optional;

public class UIEventFocus extends UIEvent implements IUIEventFocus {
	@Nullable
	protected final IUIEventTarget relatedTarget;

	public UIEventFocus(INamespacePrefixedString type, boolean canBubble, boolean cancelable, IUIEventTarget target, @Nullable IUIEventTarget relatedTarget) {
		super(type, canBubble, cancelable, target);
		this.relatedTarget = relatedTarget;
	}

	@Override
	public Optional<IUIEventTarget> getRelatedTarget() { return Optional.ofNullable(relatedTarget); }
}
