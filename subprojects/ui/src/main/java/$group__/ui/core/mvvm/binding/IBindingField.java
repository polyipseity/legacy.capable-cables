package $group__.ui.core.mvvm.binding;

import $group__.utilities.CastUtilities;
import $group__.utilities.interfaces.IHasGenericClass;
import $group__.utilities.interfaces.IValue;
import $group__.utilities.reactive.DisposableObserverAuto;
import io.reactivex.rxjava3.observers.DisposableObserver;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public interface IBindingField<T>
		extends IField<T>, IHasGenericClass<T>, IHasBindingKey {
	static <T> DisposableObserver<IValue<T>> createSynchronizationObserver(IBindingField<T> from, Iterable<? extends IBindingField<?>> to, Map<Class<?>, Map<Class<?>, Function<?, ?>>> transformers, AtomicBoolean isSource) {
		return new DisposableObserverAuto<IValue<T>>() {
			@Override
			public void onNext(@Nonnull IValue<T> t) {
				if (isSource.getAndSet(false)) {
					for (IBindingField<?> k : to) {
						if (!k.equals(from)) {
							try {
								k.setValue(CastUtilities.castUncheckedNullable(
										IBinder.transform(transformers, t.getValue().orElse(null), from.getGenericClass(), k.getGenericClass()))); // COMMENT should be of the correct type
							} catch (BindingTransformerNotFoundException ex) {
								onError(ex);
								isSource.set(true);
								break;
							}
						}
					}
					isSource.set(true);
				}
			}
		};
	}

	@Override
	default Class<T> getGenericClass() { return getField().getGenericClass(); }

	IObservableField<T> getField();

	@Override
	default Optional<? extends T> getValue() { return getField().getValue(); }

	@Override
	default void setValue(@Nullable T value) { getField().setValue(value); }
}
