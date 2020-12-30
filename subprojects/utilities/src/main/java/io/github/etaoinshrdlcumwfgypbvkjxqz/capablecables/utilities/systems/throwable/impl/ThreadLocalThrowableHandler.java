package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.def.IThrowableHandler;

import java.util.Optional;

public class ThreadLocalThrowableHandler<T extends Throwable>
		implements IThrowableHandler<T> {
	private final ThreadLocal<T> throwableThreadLocal = new ThreadLocal<>();

	@Override
	public Optional<? extends T> get() { return Optional.ofNullable(getThrowableThreadLocal().get()); }

	@Override
	public void clear() { getThrowableThreadLocal().set(null); }

	@Override
	public void catch_(T throwable) { getThrowableThreadLocal().set(throwable); }

	protected ThreadLocal<T> getThrowableThreadLocal() { return throwableThreadLocal; }
}
