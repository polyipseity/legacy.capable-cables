package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AbstractDelegatingObject;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.def.IThrowableHandler;

import java.util.Optional;

public class DelegatingThrowableHandler<T extends Throwable>
		extends AbstractDelegatingObject<IThrowableHandler<T>>
		implements IThrowableHandler<T> {
	public DelegatingThrowableHandler(IThrowableHandler<T> delegated) { super(delegated); }

	@Override
	public Optional<? extends T> get() { return getDelegate().get(); }

	@Override
	public void clear() { getDelegate().clear(); }

	@Override
	public void catch_(T throwable) { getDelegate().catch_(throwable); }
}
