package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.reactive.impl;

import org.reactivestreams.Subscriber;

public class ContinuouslyRequestingSubscriber<T>
		extends DelegatingSubscriber<T> {
	protected ContinuouslyRequestingSubscriber(Subscriber<? super T> delegate) {
		super(delegate);
	}

	public static <T> ContinuouslyRequestingSubscriber<T> of(Subscriber<? super T> delegate) {
		return new ContinuouslyRequestingSubscriber<>(delegate);
	}

	@Override
	protected void onStart() {
		super.onStart();
		request(1L);
	}

	@Override
	public void onNext(T t) {
		request(1L);
		super.onNext(t);
	}
}
