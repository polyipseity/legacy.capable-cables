package $group__.utilities.reactive;

import $group__.utilities.ThrowableUtilities.ThrowableCatcher;
import $group__.utilities.UtilitiesConfiguration;
import io.reactivex.rxjava3.observers.DisposableObserver;

import javax.annotation.Nonnull;

public abstract class DisposableObserverAuto<T>
		extends DisposableObserver<T> {
	@Override
	public void onError(@Nonnull Throwable e) {
		ThrowableCatcher.log(e, UtilitiesConfiguration.INSTANCE.getLogger());
		dispose();
	}

	@Override
	public void onComplete() { dispose(); }
}
