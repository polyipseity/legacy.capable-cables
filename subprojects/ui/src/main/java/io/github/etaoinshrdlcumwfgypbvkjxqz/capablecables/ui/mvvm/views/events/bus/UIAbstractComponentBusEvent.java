package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.events.bus;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.AbstractBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.IHookEvent;

import javax.annotation.Nullable;

public abstract class UIAbstractComponentBusEvent<T>
		extends AbstractBusEvent<T>
		implements IHookEvent {
	private final IUIComponent component;

	protected UIAbstractComponentBusEvent(@Nullable Class<T> genericType, IUIComponent component) {
		super(genericType);
		this.component = component;
	}

	public IUIComponent getComponent() { return component; }
}
