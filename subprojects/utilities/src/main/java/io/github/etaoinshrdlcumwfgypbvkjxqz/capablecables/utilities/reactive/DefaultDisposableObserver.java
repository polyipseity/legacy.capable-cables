package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class DefaultDisposableObserver<T>
		extends DisposableObserver<T> {
	@Override
	public void onNext(@Nonnull T t) {}

	@Override
	public void onError(@Nonnull Throwable e) { dispose(); }

	@Override
	public void onComplete() { dispose(); }
}
