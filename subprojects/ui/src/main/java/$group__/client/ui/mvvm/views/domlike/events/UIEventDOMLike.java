package $group__.client.ui.mvvm.views.domlike.events;

import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;

import javax.annotation.Nullable;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public class UIEventDOMLike implements Event {
	@Nullable
	protected String type;
	protected boolean bubbles, cancelable;
	protected long timeStamp;
	protected boolean defaultPrevented, propagationStopped;
	protected short eventPhase;
	protected boolean initialized, dispatching;

	@Nullable
	protected EventTarget target = null, currentTarget = null;

	@Override
	@Nullable
	public String getType() { return type; }

	@Override
	@Nullable
	public EventTarget getTarget() { return target; }

	@Override
	@Nullable
	public EventTarget getCurrentTarget() { return currentTarget; }

	@Override
	public short getEventPhase() { return eventPhase; }

	@Override
	public boolean getBubbles() { return bubbles; }

	@Override
	public boolean getCancelable() {
		return false;
	}

	@Override
	public long getTimeStamp() { return timeStamp; }

	@Override
	public void stopPropagation() { setPropagationStopped(true); }

	@Override
	public void preventDefault() { setDefaultPrevented(true); }

	@Override
	public void initEvent(String eventTypeArg, boolean canBubbleArg, boolean cancelableArg) {
		setType(eventTypeArg);
		setBubbles(canBubbleArg);
		setCancelable(cancelableArg);

		setTimeStamp(System.currentTimeMillis());
		setInitialized(true);
	}

	protected void setTimeStamp(long timeStamp) { this.timeStamp = timeStamp; }

	protected void setCancelable(boolean cancelable) { this.cancelable = cancelable; }

	protected void setBubbles(boolean bubbles) { this.bubbles = bubbles; }

	public void setEventPhase(short eventPhase) { this.eventPhase = eventPhase; }

	public void setCurrentTarget(@Nullable EventTarget currentTarget) { this.currentTarget = currentTarget; }

	public void setTarget(@Nullable EventTarget target) { this.target = target; }

	protected void setType(String type) { this.type = type; }

	public boolean isInitialized() { return initialized; }

	protected void setInitialized(boolean initialized) { this.initialized = initialized; }

	public boolean isDefaultPrevented() { return defaultPrevented; }

	protected void setDefaultPrevented(@SuppressWarnings("SameParameterValue") boolean defaultPrevented) { this.defaultPrevented = defaultPrevented; }

	public boolean isPropagationStopped() { return propagationStopped; }

	protected void setPropagationStopped(@SuppressWarnings("SameParameterValue") boolean propagationStopped) { this.propagationStopped = propagationStopped; }

	public boolean isDispatching() { return dispatching; }

	public void setDispatching(boolean dispatching) { this.dispatching = dispatching; }
}
