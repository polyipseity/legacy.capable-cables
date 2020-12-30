package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.events.IUIEvent;

import java.util.function.Consumer;

public class UIFunctionalEventListener<E extends IUIEvent>
		extends UIAbstractEventListener<E> {
	private final Consumer<@Nonnull E> action;

	public UIFunctionalEventListener(Consumer<@Nonnull E> action) { this.action = action; }

	@Override
	protected void accept0(E event) { getAction().accept(event); }

	protected Consumer<@Nonnull E> getAction() { return action; }
}
