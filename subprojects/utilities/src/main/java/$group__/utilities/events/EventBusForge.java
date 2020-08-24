package $group__.utilities.events;

import $group__.utilities.ThrowableUtilities;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.Subject;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class EventBusForge
		extends Subject<Event> {
	public static final Subject<Event> FORGE_EVENT_BUS = new EventBusForge(Bus.FORGE.bus().get());
	private static final ThreadLocal<Subject<Event>> MOD_EVENT_BUS = ThreadLocal.withInitial(() ->
			new EventBusForge(Bus.MOD.bus().get()));

	public static Subject<Event> getModEventBus() {
		@Nullable Subject<Event> ret = MOD_EVENT_BUS.get();
		assert ret != null;
		return ret;
	}

	protected final IEventBus delegated;

	public EventBusForge(IEventBus delegated) { this.delegated = delegated; }

	@Override
	public boolean hasObservers() { return true; }

	@Override
	public boolean hasThrowable() { return false; }

	@Override
	public boolean hasComplete() { return false; }

	@Override
	@Nullable
	public Throwable getThrowable() { return null; }

	@Override
	protected void subscribeActual(@Nonnull Observer<? super Event> observer) {
		@SuppressWarnings("unchecked") Observer<? extends Event> oa =
				(Observer<? extends Event>) observer; // COMMENT consider parameter 'observer' 'Observer<? extends Event>' instead, this works due to type erasure
		EventBusBridgeMethodFixConsumer<? extends Event, Observer<? extends Event>> oc;
		try {
			oc = new EventBusBridgeMethodFixConsumer<>(Observer.class, "onNext", oa);
			oc.register(getDelegated());
		} catch (Throwable t) {
			onError(t);
			return;
		}
		observer.onSubscribe(new EventBusForgeDisposable(getDelegated(), oc.getEventType(), oc));
	}

	protected IEventBus getDelegated() { return delegated; }

	@Override
	public void onSubscribe(@Nonnull Disposable d) { throw new UnsupportedOperationException(); }

	@Override
	public void onNext(@Nonnull Event o) { getDelegated().post(o); }

	@Override
	public void onError(@Nonnull Throwable e) { throw ThrowableUtilities.propagate(e); }

	@Override
	public void onComplete() { throw new UnsupportedOperationException(); }

	protected static class EventBusForgeDisposable
			implements Disposable {
		protected final AtomicBoolean disposed = new AtomicBoolean();
		protected final IEventBus owner;
		protected final Class<? extends Event> eventType;
		protected final Consumer<? extends Event> downstream;

		public EventBusForgeDisposable(IEventBus owner, Class<? extends Event> eventType, Consumer<? extends Event> downstream) {
			this.owner = owner;
			this.eventType = eventType;
			this.downstream = downstream;
		}

		@Override
		public void dispose() {
			if (!getDisposed().getAndSet(true)) {
				getOwner().unregister(getDownstream());
				EventUtilities.cleanListenersCache(getOwner(), getEventType());
			}
		}

		@Override
		public boolean isDisposed() { return getDisposed().get(); }

		protected AtomicBoolean getDisposed() { return disposed; }

		public IEventBus getOwner() { return owner; }

		protected Consumer<? extends Event> getDownstream() { return downstream; }

		protected Class<? extends Event> getEventType() { return eventType; }
	}
}