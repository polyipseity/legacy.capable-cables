package $group__.ui.core.mvvm.binding;

import $group__.utilities.CastUtilities;
import $group__.utilities.interfaces.IHasGenericClass;
import $group__.utilities.reactive.DisposableObserverAuto;
import io.reactivex.rxjava3.observers.DisposableObserver;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public interface IBindingField<T> extends IField<T>, IHasGenericClass<T>, IHasBindingKey {
	static <T> DisposableObserver<T> createSynchronizationObserver(IBindingField<T> from, Iterable<? extends IBindingField<?>> to, Map<Class<?>, Map<Class<?>, Function<?, ?>>> transformers, AtomicBoolean isSource) {
		return new DisposableObserverAuto<T>() {
			@Override
			public void onNext(@Nonnull T t) {
				if (isSource.getAndSet(false)) {
					for (IBindingField<?> k : to) {
						if (!k.equals(from)) {
							Optional<? extends Function<T, ?>> ts = IBinder.getTransformer(transformers, from.getGenericClass(), k.getGenericClass());
							if (!ts.isPresent()) {
								onError(new BindingTransformerNotFoundException(
										"Cannot find transformer for '" + from.getGenericClass() + "' -> '" + k.getGenericClass() + "' in transformers:" + System.lineSeparator()
												+ transformers));
								isSource.set(true);
								break;
							}
							k.setValue(CastUtilities.castUnchecked(ts.get().apply(t))); // COMMENT should be of the correct type
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
	default T getValue() { return getField().getValue(); }

	@Override
	default void setValue(T value) { getField().setValue(value); }
}
