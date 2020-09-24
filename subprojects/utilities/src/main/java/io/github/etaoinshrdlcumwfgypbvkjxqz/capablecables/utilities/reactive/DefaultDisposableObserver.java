package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class DefaultDisposableObserver<T>
		extends DisposableObserver<T> {
	@Override
	public void onNext(@NonNull T t) {}

	@Override
	public void onError(@NonNull Throwable e) { dispose(); }

	@Override
	public void onComplete() { dispose(); }
}
