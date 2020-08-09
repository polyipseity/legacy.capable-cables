package $group__.client.ui.mvvm.views.domlike.events;

import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

public abstract class UIEventListenerDOMLike implements EventListener {
	protected boolean removed = false;

	@Override
	public void handleEvent(Event evt) {
		if (!isRemoved())
			handleEvent0(evt);
	}

	public boolean isRemoved() { return removed; }

	protected abstract void handleEvent0(Event event);

	public void markAsRemoved() { this.removed = true; }
}
