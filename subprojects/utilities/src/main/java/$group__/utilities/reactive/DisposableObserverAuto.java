package $group__.utilities.reactive;

import $group__.utilities.ThrowableUtilities;
import io.reactivex.rxjava3.observers.DisposableObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

public abstract class DisposableObserverAuto<T>
		extends DisposableObserver<T> {
	private static final Logger LOGGER = LogManager.getLogger();

	@Override
	public void onError(@Nonnull Throwable e) {
		ThrowableUtilities.ThrowableCatcher.log(e, LOGGER);
		dispose();
	}

	@Override
	public void onComplete() { dispose(); }
}
