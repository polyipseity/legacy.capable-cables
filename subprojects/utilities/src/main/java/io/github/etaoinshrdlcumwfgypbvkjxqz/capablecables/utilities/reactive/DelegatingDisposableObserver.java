package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.reactivex.rxjava3.observers.DisposableObserver;

import javax.annotation.OverridingMethodsMustInvokeSuper;

public class DelegatingDisposableObserver<T>
		extends DisposableObserver<T> {
	private final DisposableObserver<? super T> delegate;

	protected DelegatingDisposableObserver() { this(new DefaultDisposableObserver<>()); }

	public DelegatingDisposableObserver(DisposableObserver<? super T> delegate) { this.delegate = delegate; }

	@Override
	@OverridingMethodsMustInvokeSuper
	protected void onStart() { getDelegate().onSubscribe(this); }

	protected DisposableObserver<? super T> getDelegate() { return delegate; }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onNext(@Nonnull T t) { getDelegate().onNext(t); }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onError(@Nonnull Throwable e) { getDelegate().onError(e); }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onComplete() { getDelegate().onComplete(); }
}
