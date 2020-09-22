package $group__.utilities.reactive;

import $group__.utilities.AbstractDelegatingObject;
import io.reactivex.rxjava3.disposables.Disposable;

public class DelegatingDisposable
		extends AbstractDelegatingObject<Disposable>
		implements Disposable {
	public DelegatingDisposable(Disposable delegate) { super(delegate); }

	@Override
	public void dispose() { getDelegate().dispose(); }

	@Override
	public boolean isDisposed() { return getDelegate().isDisposed(); }
}
