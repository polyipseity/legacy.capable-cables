package $group__.client.ui.mvvm.views.events.ui;

import $group__.client.ui.events.ui.UIEvent;
import $group__.client.ui.mvvm.core.views.events.IUIEventFocus;
import $group__.client.ui.mvvm.core.views.events.IUIEventTarget;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Optional;

public class UIEventFocus extends UIEvent implements IUIEventFocus {
	@Nullable
	protected final IUIEventTarget relatedTarget;

	public UIEventFocus(ResourceLocation type, boolean canBubble, IUIEventTarget target, @Nullable IUIEventTarget relatedTarget) {
		super(type, canBubble, target);
		this.relatedTarget = relatedTarget;
	}

	@Override
	public Optional<IUIEventTarget> getRelatedTarget() { return Optional.ofNullable(relatedTarget); }
}
