package $group__.client.ui.mvvm.core.binding;

import $group__.utilities.interfaces.IHasGenericClass;
import $group__.utilities.reactive.DisposableObserverAuto;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.observers.DisposableObserver;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public interface IBindingMethod<T> extends IHasGenericClass<T>, IHasBindingKey {
	EnumType getType();

	enum EnumType {
		SOURCE,
		DESTINATION
	}

	interface ISource<T> extends IBindingMethod<T> {
		static <T> DisposableObserver<T> createDelegatingObserver(Iterable<IDestination<T>> destinations) {
			return new DisposableObserverAuto<T>() {
				@Override
				public void onNext(@Nonnull T t) { destinations.forEach(d -> d.accept(t)); }
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
