package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.events.ui;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEvent;

import java.util.function.Consumer;

public class UIFunctionalEventListener<E extends IUIEvent>
		extends UIAbstractEventListener<E> {
	private final Consumer<E> action;

	public UIFunctionalEventListener(Consumer<E> action) { this.action = action; }

	@Override
	protected void accept0(E event) { getAction().accept(event); }

	protected Consumer<E> getAction() { return action; }
}
