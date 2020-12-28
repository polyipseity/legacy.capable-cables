package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.reactive.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AbstractDelegatingObject;
import io.reactivex.rxjava3.disposables.Disposable;

public class DelegatingDisposable
		extends AbstractDelegatingObject<Disposable>
		implements Disposable {
	protected DelegatingDisposable(Disposable delegate) { super(delegate); }

	@Override
	public void dispose() { getDelegate().dispose(); }

	@Override
	public boolean isDisposed() { return getDelegate().isDisposed(); }
}
