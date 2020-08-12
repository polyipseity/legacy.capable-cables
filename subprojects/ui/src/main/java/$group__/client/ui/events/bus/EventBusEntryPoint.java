package $group__.client.ui.events.bus;

import $group__.utilities.CastUtilities;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Optional;

// TODO find a way to allow any arbitrary event bus to be set as the delegated
public enum EventBusEntryPoint
		implements IUIEventBus<Object, Object> {
	INSTANCE,
	;

	protected transient final Object lockObject = new Object();
	@Nullable
	protected IUIEventBus<?, ?> delegated;

	@Override
	public boolean register(Object target) {
		return getDelegated().filter(d ->
				d.register(CastUtilities.castUnchecked(d.getTargetClass().cast(target))) // COMMENT exception will be thrown by .cast
		).isPresent();
	}

	@Override
	public boolean unregister(Object target) {
		return getDelegated().filter(d ->
				d.unregister(CastUtilities.castUnchecked(d.getTargetClass().cast(target))) // COMMENT exception will be thrown by .cast
		).isPresent();
	}

	@Override
	public boolean post(Object event) {
		return getDelegated().filter(d ->
				d.post(CastUtilities.castUnchecked(d.getEventClass().cast(event))) // COMMENT exception will be thrown by .cast
		).isPresent();
	}

	@Override
	public Class<Object> getEventClass() { throw new UnsupportedOperationException(); }

	@Override
	public Class<Object> getTargetClass() { throw new UnsupportedOperationException(); }

	public Optional<IUIEventBus<?, ?>> getDelegated() {
		synchronized (getLockObject()) {
			return Optional.ofNullable(delegated);
		}
	}

	public void setDelegated(@Nullable IUIEventBus<?, ?> delegated) {
		synchronized (getLockObject()) {
			this.delegated = delegated;
		}
	}

	protected Object getLockObject() { return lockObject; }

	private void readObject(ObjectInputStream in)
			throws IOException, ClassNotFoundException { in.defaultReadObject(); }
}
