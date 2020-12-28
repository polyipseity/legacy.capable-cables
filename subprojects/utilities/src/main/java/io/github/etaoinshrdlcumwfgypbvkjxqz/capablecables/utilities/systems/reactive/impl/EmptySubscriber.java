package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.reactive.impl;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public enum EmptySubscriber
		implements Subscriber<Object> {
	INSTANCE,
	;

	@Override
	public void onSubscribe(Subscription s) {
		// COMMENT NOOP
	}

	@Override
	public void onNext(Object o) {
		// COMMENT NOOP
	}

	@Override
	public void onError(Throwable t) {
		// COMMENT NOOP
	}

	@Override
	public void onComplete() {
		// COMMENT NOOP
	}
}
