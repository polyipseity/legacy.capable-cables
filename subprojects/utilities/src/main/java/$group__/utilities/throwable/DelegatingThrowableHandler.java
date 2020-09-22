package $group__.utilities.throwable;

import $group__.utilities.AbstractDelegatingObject;

import java.util.Optional;

public class DelegatingThrowableHandler<T extends Throwable>
		extends AbstractDelegatingObject<IThrowableHandler<T>>
		implements IThrowableHandler<T> {
	public DelegatingThrowableHandler(IThrowableHandler<T> delegated) { super(delegated); }

	@Override
	public void catch_(T throwable) { getDelegate().catch_(throwable); }

	@Override
	public Optional<? extends T> get() { return getDelegate().get(); }

	@Override
	public void clear() { getDelegate().clear(); }
}
