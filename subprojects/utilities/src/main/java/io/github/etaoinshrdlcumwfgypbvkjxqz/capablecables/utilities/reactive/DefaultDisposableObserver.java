package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class DefaultDisposableObserver<T>
		extends DisposableObserver<T> {
	private static final DefaultDisposableObserver<Object> EMPTY = new DefaultDisposableObserver<>();

	@SuppressWarnings("unchecked")
	public static <T> DefaultDisposableObserver<T> getEmpty() { return (DefaultDisposableObserver<T>) EMPTY; }

	@Override
	public void onNext(@NonNull T t) {}

	@Override
	public void onError(@NonNull Throwable e) { dispose(); }

	@Override
	public void onComplete() { dispose(); }
}
