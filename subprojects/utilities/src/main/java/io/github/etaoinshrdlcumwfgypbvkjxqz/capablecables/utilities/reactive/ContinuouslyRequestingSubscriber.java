package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive;

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
		request(Long.MAX_VALUE);
	}

	@Override
	public void onNext(T t) {
		request(1L); // COMMENT in case the subscription allows for requests equal to and greater than 'Long.MAX_VALUE'
		super.onNext(t);
	}
}
