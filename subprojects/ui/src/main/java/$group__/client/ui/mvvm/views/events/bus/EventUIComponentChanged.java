package $group__.client.ui.mvvm.views.events.bus;

import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.utilities.events.EnumEventHookStage;

public abstract class EventUIComponentChanged<T> extends EventUIComponent {
	protected final T previous, next;

	protected EventUIComponentChanged(EnumEventHookStage stage, IUIComponent component, T previous, T next) {
		super(stage, component);
		this.previous = previous;
		this.next = next;
	}

	public T getPrevious() { return previous; }

	public T getNext() { return next; }
}
