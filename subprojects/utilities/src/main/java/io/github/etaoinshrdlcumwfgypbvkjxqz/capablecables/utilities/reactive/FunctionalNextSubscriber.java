package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive;

import org.reactivestreams.Subscriber;

import java.util.function.Consumer;

public class FunctionalNextSubscriber<T>
		extends DelegatingSubscriber<T> {
	private final Consumer<? super T> action;

	protected FunctionalNextSubscriber(Subscriber<? super T> delegate, Consumer<? super T> action) {
		super(delegate);
		this.action = action;
	}

	public static <T> FunctionalNextSubscriber<T> of(Subscriber<? super T> delegate, Consumer<? super T> action) {
		return new FunctionalNextSubscriber<>(delegate, action);
	}

	@Override
	public void onNext(T t) {
		super.onNext(t);
		getAction().accept(t);
	}

	protected Consumer<? super T> getAction() {
		return action;
	}
}
