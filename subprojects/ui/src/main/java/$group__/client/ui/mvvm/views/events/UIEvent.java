package $group__.client.ui.mvvm.views.events;

import $group__.client.ui.mvvm.views.core.events.IUIEvent;
import $group__.client.ui.mvvm.views.core.events.IUIEventTarget;
import net.minecraft.util.ResourceLocation;

public class UIEvent implements IUIEvent {
	protected final ResourceLocation type;
	protected final boolean canBubble;
	protected final IUIEventTarget target;
	protected EnumPhase phase = EnumPhase.NONE;
	protected boolean propagationStopped = false;

	public UIEvent(ResourceLocation type, boolean canBubble, IUIEventTarget target) {
		this.type = type;
		this.canBubble = canBubble;
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
	}

	@Override
	public boolean canBubble() { return canBubble; }

	@Override
	public boolean isPropagationStopped() { return propagationStopped; }

	protected void setPropagationStopped(@SuppressWarnings("SameParameterValue") boolean propagationStopped) { this.propagationStopped = propagationStopped; }

	protected void setPhase(EnumPhase phase) { this.phase = phase; }

}
