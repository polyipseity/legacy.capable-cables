package $group__.client.ui.mvvm.views.events;

import $group__.client.ui.mvvm.views.core.events.IUIEvent;
import $group__.client.ui.mvvm.views.core.events.IUIEventListener;

public abstract class UIEventListener<E extends IUIEvent> implements IUIEventListener<E> {
	protected boolean removed = false;

	@Override
	public void markAsRemoved() { setRemoved(true); }

	protected void setRemoved(@SuppressWarnings("SameParameterValue") boolean removed) { this.removed = removed; }

	public boolean isRemoved() { return removed; }

	@Override
	public void accept(E event) {
		if (!isRemoved())
			accept0(event);
	}

	protected abstract void accept0(E event);
}
