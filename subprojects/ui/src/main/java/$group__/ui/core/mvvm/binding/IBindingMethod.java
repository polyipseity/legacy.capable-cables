package $group__.ui.core.mvvm.binding;

import $group__.utilities.CastUtilities;
import $group__.utilities.interfaces.IHasGenericClass;
import $group__.utilities.reactive.DisposableObserverAuto;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.observers.DisposableObserver;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public interface IBindingMethod<T> extends IHasGenericClass<T>, IHasBindingKey {
	EnumType getType();

	enum EnumType {
		SOURCE,
		DESTINATION
	}

	interface Source<T> extends IBindingMethod<T> {
		static <T> DisposableObserver<T> createDelegatingObserver(Source<T> source, Iterable<? extends Destination<?>> destination, Map<Class<?>, Map<Class<?>, Function<?, ?>>> transformers) {
			return new DisposableObserverAuto<T>() {
				@Override
				public void onNext(@Nonnull T t) {
					for (Destination<?> d : destination) {
						try {
							d.accept(CastUtilities.castUncheckedNullable(
									IBinder.transform(transformers, t, source.getGenericClass(), d.getGenericClass()))); // COMMENT should be of the correct type
						} catch (BindingTransformerNotFoundException ex) {
							onError(ex);
							break;
						}
					}
				}
			};
		}

		@Override
		default EnumType getType() { return EnumType.SOURCE; }

		ObservableSource<T> getNotifier();

		void invoke(T argument);
	}

	interface Destination<T>
			extends IBindingMethod<T>, Consumer<T> {
		@Override
		default EnumType getType() { return EnumType.DESTINATION; }

		@Override
		void accept(T argument);
	}
}
