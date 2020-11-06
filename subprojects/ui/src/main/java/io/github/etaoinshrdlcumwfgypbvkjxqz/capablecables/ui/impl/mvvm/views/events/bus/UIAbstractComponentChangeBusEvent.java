package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.bus;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.core.IHookEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.EnumHookStage;

public abstract class UIAbstractComponentChangeBusEvent<T, V>
		extends UIAbstractComponentBusEvent<T>
		implements IHookEvent {
	private final EnumHookStage stage;
	private final V previous, next;

	protected UIAbstractComponentChangeBusEvent(@Nullable Class<T> genericType, EnumHookStage stage, IUIComponent component, V previous, V next) {
		super(genericType, component);
		this.stage = stage;
		this.previous = previous;
		this.next = next;
	}

	@Override
	public EnumHookStage getStage() {
		return stage;
	}

	public V getPrevious() { return previous; }

	public V getNext() { return next; }
}
