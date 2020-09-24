package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

import java.lang.ref.ReferenceQueue;

public class DisposableObserverWeakReference<T, R extends Disposable & Observer<T>>
		extends OptionalWeakReference<R>
		implements Disposable, Observer<T> {
	public DisposableObserverWeakReference(R referent) { super(referent); }

	public DisposableObserverWeakReference(R referent, ReferenceQueue<? super R> q) { super(referent, q); }

	@Override
	public void dispose() {
		getOptional()
				.ifPresent(Disposable::dispose);
	}

	@Override
	public boolean isDisposed() {
		return getOptional()
				.map(Disposable::isDisposed)
				.orElse(true);
	}

	@Override
	public void onSubscribe(@NonNull Disposable d) {
		getOptional()
				.ifPresent(observer -> observer.onSubscribe(d));
	}

	@Override
	public void onNext(@NonNull T t) {
		getOptional()
				.ifPresent(observer -> observer.onNext(t));
	}

	@Override
	public void onError(@NonNull Throwable e) {
		getOptional()
				.ifPresent(observer -> observer.onError(e));
	}

	@Override
	public void onComplete() {
		getOptional()
				.ifPresent(R::onComplete);
	}
}
