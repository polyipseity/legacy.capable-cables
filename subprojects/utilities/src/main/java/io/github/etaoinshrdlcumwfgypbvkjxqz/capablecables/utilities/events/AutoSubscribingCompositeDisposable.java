package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.DelegatingDisposable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.Subject;

public class AutoSubscribingCompositeDisposable<DO extends Disposable & Observer<?>>
		extends DelegatingDisposable {
	@SafeVarargs
	public AutoSubscribingCompositeDisposable(Subject<?> bus, DO... listeners) { this(bus, ImmutableList.copyOf(listeners)); }

	@SuppressWarnings("UnstableApiUsage")
	public AutoSubscribingCompositeDisposable(Subject<?> bus, Iterable<? extends DO> listeners) {
		super(new CompositeDisposable(listeners));
		Streams.stream(listeners).unordered()
				.map(CastUtilities::<Observer<? super Object>>castUnchecked)
				.forEach(bus::subscribe);
	}
}
