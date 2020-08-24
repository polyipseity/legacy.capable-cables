package $group__.client.ui.events.ui;

import $group__.client.ui.core.mvvm.views.events.IUIEvent;
import $group__.client.ui.core.mvvm.views.events.IUIEventListener;

import java.util.function.Consumer;

public abstract class UIEventListener<E extends IUIEvent>
		implements IUIEventListener<E> {
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

	public static class Functional<E extends IUIEvent>
			extends UIEventListener<E> {
		protected final Consumer<E> action;

		public Functional(Consumer<E> action) { this.action = action; }

		@Override
		protected void accept0(E event) { getAction().accept(event); }

		protected Consumer<E> getAction() { return action; }
	}
}
