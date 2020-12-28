package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.reactive.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;

public class ErrorLoggingSubscriber<T>
		extends DelegatingSubscriber<T> {
	private final Logger logger;

	protected ErrorLoggingSubscriber(Subscriber<? super T> delegate, Logger logger) {
		super(delegate);
		this.logger = logger;
	}

	public static <T> ErrorLoggingSubscriber<T> of(Logger logger) {
		return of(logger, EmptySubscriber.INSTANCE);
	}

	public static <T> ErrorLoggingSubscriber<T> of(Logger logger, Subscriber<? super T> delegate) {
		return new ErrorLoggingSubscriber<>(delegate, logger);
	}

	@Override
	public void onError(Throwable t) {
		ThrowableUtilities.logCatch(t, getLogger());
		// COMMENT log first
		super.onError(t);
	}

	protected Logger getLogger() {
		return logger;
	}
}
