package $group__.client.ui.mvvm.core.binding;

import $group__.utilities.CastUtilities;
import $group__.utilities.interfaces.IHasGenericClass;
import $group__.utilities.reactive.DisposableObserverAuto;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.observers.DisposableObserver;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public interface IBindingMethod<T> extends IHasGenericClass<T>, IHasBindingKey {
	EnumType getType();

	enum EnumType {
		SOURCE,
		DESTINATION
	}

	interface ISource<T> extends IBindingMethod<T> {
		static <T, R> DisposableObserver<T> createDelegatingObserver(ISource<T> source, Iterable<? extends IDestination<?>> destination, Map<Class<?>, Map<Class<?>, Function<?, ?>>> transformers) {
			return new DisposableObserverAuto<T>() {
				@Override
				public void onNext(@Nonnull T t) {
					for (IDestination<?> d : destination) {
						Optional<? extends Function<T, ?>> ts = IBinder.getTransformer(transformers, source.getGenericClass(), d.getGenericClass());
						if (!ts.isPresent()) {
							onError(new BindingTransformerNotFoundException(
									"Cannot find transformer for '" + source.getGenericClass() + "' -> '" + d.getGenericClass() + "' in transformers:" + System.lineSeparator()
											+ transformers));
							break;
						}
						d.accept(CastUtilities.castUnchecked(ts.get().apply(t))); // COMMENT should be of the correct type
					}
				}
			};
		}

		@Override
		default EnumType getType() { return EnumType.SOURCE; }

		ObservableSource<T> getNotifier();

		void invoke(T argument);
	}

	interface IDestination<T>
			extends IBindingMethod<T>, Consumer<T> {
		@Override
		default EnumType getType() { return EnumType.DESTINATION; }

		@Override
		void accept(T argument);
	}
}
