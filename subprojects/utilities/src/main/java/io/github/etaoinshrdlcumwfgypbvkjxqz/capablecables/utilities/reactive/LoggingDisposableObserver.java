package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;
import io.reactivex.rxjava3.observers.DisposableObserver;
import org.slf4j.Logger;

import javax.annotation.OverridingMethodsMustInvokeSuper;

public class LoggingDisposableObserver<T>
		extends DelegatingDisposableObserver<T> {
	private final Logger logger;

	protected LoggingDisposableObserver(Logger logger) { this(new DefaultDisposableObserver<>(), logger); }

	public LoggingDisposableObserver(DisposableObserver<? super T> delegate, Logger logger) {
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
