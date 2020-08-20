package $group__.client.ui.events.bus;

import $group__.utilities.ThrowableUtilities;
import $group__.utilities.events.EventBusBridgeMethodFixConsumer;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.Subject;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class EventBusForge
		extends Subject<Event> {
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
		observer.onSubscribe(new EventBusForgeDisposable(getDelegated(), oc));
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
		protected final IEventBus delegated;
		protected final Consumer<? extends Event> downstream;

		public EventBusForgeDisposable(IEventBus delegated, Consumer<? extends Event> downstream) {
			this.delegated = delegated;
			this.downstream = downstream;
		}

		@Override
		public void dispose() {
			if (!getDisposed().getAndSet(true))
				getDelegated().unregister(getDownstream());
		}

		@Override
		public boolean isDisposed() { return getDisposed().get(); }

		protected AtomicBoolean getDisposed() { return disposed; }

		public IEventBus getDelegated() { return delegated; }

		protected Consumer<? extends Event> getDownstream() { return downstream; }
	}
}
