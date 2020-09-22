package $group__.utilities.reactive;

import $group__.utilities.throwable.ThrowableUtilities;
import io.reactivex.rxjava3.observers.DisposableObserver;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;

public class LoggingDisposableObserver<T>
		extends DelegatingDisposableObserver<T> {
	private final Logger logger;

	protected LoggingDisposableObserver(Logger logger) { this(DefaultDisposableObserver.getEmpty(), logger); }

	public LoggingDisposableObserver(DisposableObserver<T> delegate, Logger logger) {
		super(delegate);
		this.logger = logger;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onError(@Nonnull Throwable e) {
		ThrowableUtilities.logCatch(e, getLogger());
		super.onError(e);
	}

	protected Logger getLogger() { return logger; }
}
