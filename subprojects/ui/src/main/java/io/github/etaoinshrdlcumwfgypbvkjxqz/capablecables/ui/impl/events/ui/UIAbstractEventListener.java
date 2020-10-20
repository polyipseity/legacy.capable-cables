package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventListener;

public abstract class UIAbstractEventListener<E extends IUIEvent>
		implements IUIEventListener<E> {
	private boolean removed = false;

	@Override
	public void markAsRemoved() { setRemoved(true); }

	@Override
	public final void accept(E event) {
		if (!isRemoved())
			accept0(event);
	}

	public boolean isRemoved() { return removed; }

	protected void setRemoved(@SuppressWarnings("SameParameterValue") boolean removed) { this.removed = removed; }

	protected abstract void accept0(E event);
}
