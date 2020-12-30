package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.def.IThrowableHandler;

import java.util.Optional;

public class DefaultThrowableHandler<T extends Throwable>
		implements IThrowableHandler<T> {
	@Nullable
	private T throwable;

	@Override
	public Optional<? extends T> get() { return getThrowable(); }

	@Override
	public void clear() { setThrowable(null); }

	@Override
	public void catch_(T throwable) { setThrowable(throwable); }

	protected Optional<T> getThrowable() { return Optional.ofNullable(throwable); }

	protected void setThrowable(@Nullable T throwable) { this.throwable = throwable; }
}
