package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.events.bus;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.EnumHookStage;

public abstract class UIComponentChangedBusEvent<T> extends UIComponentBusEvent {
	protected final T previous, next;

	protected UIComponentChangedBusEvent(EnumHookStage stage, IUIComponent component, T previous, T next) {
		super(stage, component);
		this.previous = previous;
		this.next = next;
	}

	public T getPrevious() { return previous; }

	public T getNext() { return next; }
}
