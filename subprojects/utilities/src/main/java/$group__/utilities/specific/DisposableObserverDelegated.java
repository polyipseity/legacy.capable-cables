package $group__.utilities.specific;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.observers.DisposableObserver;

import javax.annotation.Nonnull;

public class DisposableObserverDelegated<T> extends DisposableObserver<T> {
	protected final Observer<T> downstream;

	public DisposableObserverDelegated(Observer<T> downstream) { this.downstream = downstream; }

	@Override
	protected void onStart() { getDownstream().onSubscribe(this); }

	protected Observer<T> getDownstream() { return downstream; }

	@Override
	public void onNext(@Nonnull T o) { getDownstream().onNext(o); }

	@Override
	public void onError(@NonNull Throwable e) { getDownstream().onError(e); }

	@Override
	public void onComplete() { getDownstream().onComplete(); }
}
