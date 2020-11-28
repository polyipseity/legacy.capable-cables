package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl;

import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.DelegatingDisposable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.Subject;

public class AutoSubscribingCompositeDisposable<DO extends Disposable & Observer<?>>
		extends DelegatingDisposable {
	@SuppressWarnings("UnstableApiUsage")
	public AutoSubscribingCompositeDisposable(Subject<?> bus, Iterable<? extends DO> listeners) {
		super(new CompositeDisposable(listeners));
		Streams.stream(listeners).unordered()
				.map(CastUtilities::<Observer<? super Object>>castUnchecked)
				.forEach(bus::subscribe);
	}
}
