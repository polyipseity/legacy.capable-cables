package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.throwable.ThrowableUtilities;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.Subject;
import net.minecraftforge.eventbus.api.BusBuilder;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class EventBusForge
		extends Subject<Event> {
	private static final Subject<Event> FORGE_EVENT_BUS = new EventBusForge(AssertionUtilities.assertNonnull(Bus.FORGE.bus().get()));
	private static final ThreadLocal<Subject<Event>> MOD_EVENT_BUS = ThreadLocal.withInitial(() ->
			new EventBusForge(AssertionUtilities.assertNonnull(Bus.MOD.bus().get())));
	private static final Subject<Event> UI_EVENT_BUS = new EventBusForge(new BusBuilder().build());

	public static Subject<Event> getModEventBus() { return AssertionUtilities.assertNonnull(MOD_EVENT_BUS.get()); }

	public static Subject<Event> getUIEventBus() { return UI_EVENT_BUS; }

	public static Subject<Event> getForgeEventBus() { return FORGE_EVENT_BUS; }

	private final IEventBus delegate;

	public EventBusForge(IEventBus delegate) { this.delegate = delegate; }

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
		ObserverToEventBusListenerAdapter<? extends Event, Observer<? extends Event>> oc;
		try {
			oc = new ObserverToEventBusListenerAdapter<>(oa, Observer.class, "onNext");
		} catch (NoSuchMethodException e) {
			onError(e);
			return;
		}
		oc.register(getDelegate());
		observer.onSubscribe(new EventBusForgeDisposable(getDelegate(), oc.getEventType(), oc));
	}

	protected IEventBus getDelegate() { return delegate; }

	@Override
	public void onSubscribe(@Nonnull Disposable d) { throw new UnsupportedOperationException(); }

	@Override
	public void onNext(@Nonnull Event o) {
		try {
			getDelegate().post(o);
		} catch (Throwable t) {
			onError(t);
		}
	}

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
				EventBusUtilities.cleanListenersCache(getOwner(), getEventType());
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
