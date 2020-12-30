package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.bus;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.def.IHookEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.AbstractBusEvent;

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
