package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;
import io.reactivex.rxjava3.subscribers.DefaultSubscriber;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.reactivestreams.Processor;
import org.reactivestreams.Subscriber;

public class EventBusProcessor
		extends DefaultSubscriber<Event>
		implements Processor<Event, Event> {
	private static final EventBusProcessor FORGE_EVENT_BUS = of(AssertionUtilities.assertNonnull(Bus.FORGE.bus().get()));
	private static final ThreadLocal<EventBusProcessor> MOD_EVENT_BUS = ThreadLocal.withInitial(() -> of(AssertionUtilities.assertNonnull(Bus.MOD.bus().get())));

	private final IEventBus delegate;

	protected EventBusProcessor(IEventBus delegate) {
		this.delegate = delegate;
	}

	public static EventBusProcessor getModEventBus() { return AssertionUtilities.assertNonnull(MOD_EVENT_BUS.get()); }

	public static EventBusProcessor getForgeEventBus() { return FORGE_EVENT_BUS; }

	public static EventBusProcessor of(IEventBus delegate) {
		return new EventBusProcessor(delegate);
	}

	@Override
	public void subscribe(Subscriber<? super Event> s) {
		@SuppressWarnings("unchecked") Subscriber<? extends Event> s1 =
				(Subscriber<? extends Event>) s; // COMMENT consider parameter 's' 'Subscriber<? extends Event>' instead, this works due to type erasure
		EventBusListenerAdapter<? extends Event> adapter;
		try {
			adapter = new EventBusListenerAdapter<>(getDelegate(), s1, Subscriber.class, "onNext");
		} catch (NoSuchMethodException e) {
			cancel();
			onError(e);
			return;
		}
		s.onSubscribe(adapter);
		adapter.register();
	}

	protected IEventBus getDelegate() {
		return delegate;
	}

	@Override
	protected void onStart() {
		super.onStart();
		getDelegate().start();
		request(Long.MAX_VALUE); // COMMENT see 'ContinuouslyRequestingSubscriber'
	}

	@Override
	public void onNext(@Nonnull Event o) {
		request(1L);
		try {
			getDelegate().post(o);
		} catch (Throwable t) {
			cancel();
			onError(t);
		}
	}

	@Override
	public void onError(@Nonnull Throwable e) {
		throw ThrowableUtilities.propagate(e);
	}

	@Override
	public void onComplete() {
		getDelegate().shutdown();
	}
}
