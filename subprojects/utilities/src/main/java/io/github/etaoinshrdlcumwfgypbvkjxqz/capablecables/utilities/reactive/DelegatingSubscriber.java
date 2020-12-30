package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive;

import io.reactivex.rxjava3.subscribers.DisposableSubscriber;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class DelegatingSubscriber<T>
		extends DisposableSubscriber<T>
		implements Subscriber<T> {
	private final Subscriber<? super T> delegate;

	protected DelegatingSubscriber(Subscriber<? super T> delegate) {
		this.delegate = delegate;
	}

	public static <T> DelegatingSubscriber<T> of(Subscriber<? super T> delegate) {
		return new DelegatingSubscriber<>(delegate);
	}

	@Override
	protected void onStart() {
		getDelegate().onSubscribe(new SelfDelegatingSubscription());
	}

	protected Subscriber<? super T> getDelegate() {
		return delegate;
	}

	@Override
	public void onNext(T t) {
		getDelegate().onNext(t);
	}

	@Override
	public void onError(Throwable t) {
		getDelegate().onError(t);
	}

	@Override
	public void onComplete() {
		getDelegate().onComplete();
	}

	protected class SelfDelegatingSubscription
			implements Subscription {
		@Override
		public void request(long n) {
			DelegatingSubscriber.this.request(n);
		}

		@Override
		public void cancel() {
			DelegatingSubscriber.this.cancel();
		}
	}
}
