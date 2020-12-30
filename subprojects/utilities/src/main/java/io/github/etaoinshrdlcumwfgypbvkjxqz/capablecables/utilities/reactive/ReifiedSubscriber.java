package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive;

import org.reactivestreams.Subscriber;

public abstract class ReifiedSubscriber<T>
		extends DelegatingSubscriber<T> {
	protected ReifiedSubscriber(Subscriber<? super T> delegate) {
		super(delegate);
	}

	@Override
	public abstract void onNext(T t); // COMMENT allows for reifying 'T' through the method as well

	@SuppressWarnings("unused")
	protected final void onNextImpl(T t) {
		super.onNext(t);
	}
}
