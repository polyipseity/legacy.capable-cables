package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl;

import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.reactive.impl.DelegatingDisposable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public class AutoSubscribingDisposable
		extends DelegatingDisposable {
	@SuppressWarnings("UnstableApiUsage")
	protected <DS extends Subscriber<?> & Disposable> AutoSubscribingDisposable(Publisher<?> bus, Iterable<? extends DS> listeners) {
		super(new CompositeDisposable(listeners));
		Streams.stream(listeners).unordered()
				.map(CastUtilities::<Subscriber<? super Object>>castUnchecked)
				.forEach(bus::subscribe);
	}

	public static <DS extends Subscriber<?> & Disposable> AutoSubscribingDisposable of(Publisher<?> bus,
	                                                                                   Iterable<? extends DS> listeners) {
		return new AutoSubscribingDisposable(bus, listeners);
	}
}
