package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.reactive.impl;

import org.reactivestreams.Subscriber;

public abstract class ReifiedSubscriber<T>
		extends DelegatingSubscriber<T> {
	protected ReifiedSubscriber(Subscriber<? super T> delegate) {
		super(delegate);
	}
}
