package $group__.client.ui.events.ui;

import $group__.client.ui.mvvm.core.views.events.IUIEvent;
import $group__.client.ui.mvvm.core.views.events.IUIEventTarget;
import net.minecraft.util.ResourceLocation;

import java.util.concurrent.atomic.AtomicBoolean;

public class UIEvent implements IUIEvent {
	protected final ResourceLocation type;
	protected final boolean
			canBubble,
			cancelable;
	protected final IUIEventTarget target;
	protected EnumPhase phase = EnumPhase.NONE;
	protected boolean propagationStopped = false;
	protected AtomicBoolean defaultPrevented = new AtomicBoolean();

	public UIEvent(ResourceLocation type, boolean canBubble, boolean cancelable, IUIEventTarget target) {
		this.type = type;
		this.canBubble = canBubble;
		this.cancelable = cancelable;
		this.target = target;
	}

	@Override
	public ResourceLocation getType() { return type; }

	@Override
	public IUIEventTarget getTarget() { return target; }

	@Override
	public EnumPhase getPhase() { return phase; }

	@Override
	public void advancePhase() { setPhase(EnumPhase.values()[(phase.ordinal() + 1) % EnumPhase.values().length]); }

	@Override
	public void stopPropagation() { setPropagationStopped(true); }

	@Override
	public void reset() {
		setPhase(EnumPhase.NONE);
		setPropagationStopped(false);
		getDefaultPrevented().set(false);
	}

	@Override
	public boolean isCancelable() { return cancelable; }

	@Override
	public boolean canBubble() { return canBubble; }

	@Override
	public boolean isDefaultPrevented() { return getDefaultPrevented().get(); }

	@Override
	public boolean isPropagationStopped() { return propagationStopped; }

	@Override
	public boolean preventDefault() { return isCancelable() && !getDefaultPrevented().getAndSet(true); }

	protected void setPropagationStopped(boolean propagationStopped) { this.propagationStopped = propagationStopped; }

	protected void setPhase(EnumPhase phase) { this.phase = phase; }

	protected AtomicBoolean getDefaultPrevented() { return defaultPrevented; }
}
