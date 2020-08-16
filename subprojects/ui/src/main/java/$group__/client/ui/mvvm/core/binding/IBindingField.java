package $group__.client.ui.mvvm.core.binding;

import $group__.utilities.interfaces.IHasGenericClass;
import $group__.utilities.reactive.DisposableObserverAuto;
import io.reactivex.rxjava3.observers.DisposableObserver;

import javax.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicBoolean;

public interface IBindingField<T> extends IField<T>, IHasGenericClass<T>, IHasBindingKey {
	static <T> DisposableObserver<T> createSynchronizationObserver(IBindingField<T> from, Iterable<IBindingField<T>> to, AtomicBoolean isSource) {
		return new DisposableObserverAuto<T>() {
			@Override
			public void onNext(@Nonnull T o) {
				if (isSource.getAndSet(false)) {
					to.forEach(k -> {
						if (!k.equals(from))
							k.setValue(o);
					});
					isSource.set(true);
				}
			}
		};
	}

	@Override
	default Class<T> getGenericClass() { return getField().getGenericClass(); }

	IObservableField<T> getField();

	@Override
	default T getValue() { return getField().getValue(); }

	@Override
	default void setValue(T value) { getField().setValue(value); }
}

