package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.events.IUIEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.events.IUIEventTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;

import java.util.concurrent.atomic.AtomicBoolean;

public class UIDefaultEvent implements IUIEvent {
	private final IIdentifier type;
	private final boolean canBubble;
	private final boolean cancelable;
	private final IUIViewContext viewContext;
	private final IUIEventTarget target;
	private final AtomicBoolean defaultPrevented = new AtomicBoolean();
	private EnumPhase phase = EnumPhase.NONE;
	private boolean propagationStopped = false;

	public UIDefaultEvent(IIdentifier type, boolean canBubble, boolean cancelable, IUIViewContext viewContext, IUIEventTarget target) {
		this.type = type;
		this.canBubble = canBubble;
		this.cancelable = cancelable;
		this.viewContext = viewContext;
		this.target = target;
	}

	@Override
	public IIdentifier getType() { return type; }

	@Override
	public IUIEventTarget getTarget() { return target; }

	@Override
	public EnumPhase getPhase() { return phase; }

	@Override
	public void advancePhase()
			throws IllegalStateException { setPhase(getPhase().getNextPhase()); }

	@Override
	public void reset() {
		setPhase(EnumPhase.NONE);
		setPropagationStopped(false);
		getDefaultPrevented().set(false);
	}

	@Override
	public boolean canBubble() { return canBubble; }

	@Override
	public boolean isPropagationStopped() { return propagationStopped; }

	@Override
	public void stopPropagation() { setPropagationStopped(true); }

	@Override
	public boolean isCancelable() { return cancelable; }

	@Override
	public boolean isDefaultPrevented() { return getDefaultPrevented().get(); }

	@Override
	public boolean preventDefault() { return isCancelable() && !getDefaultPrevented().getAndSet(true); }

	@Override
	public @Immutable IUIViewContext getViewContext() { return viewContext; }

	protected void setPropagationStopped(boolean propagationStopped) { this.propagationStopped = propagationStopped; }

	protected AtomicBoolean getDefaultPrevented() { return defaultPrevented; }

	protected void setPhase(EnumPhase phase) { this.phase = phase; }
}
