package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.events.ui;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventListener;

public abstract class AbstractUIEventListener<E extends IUIEvent>
		implements IUIEventListener<E> {
	private boolean removed = false;

	@Override
	public void markAsRemoved() { setRemoved(true); }

	protected void setRemoved(@SuppressWarnings("SameParameterValue") boolean removed) { this.removed = removed; }

	public boolean isRemoved() { return removed; }

	@Override
	public final void accept(E event) {
		if (!isRemoved())
			accept0(event);
	}

	protected abstract void accept0(E event);
}
