package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.events.bus;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.EnumHookStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.IHookEvent;

import javax.annotation.Nullable;

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
