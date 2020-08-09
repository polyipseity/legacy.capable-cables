package $group__.client.ui.mvvm.core.binding;

import $group__.utilities.interfaces.IHasGenericClass;
import $group__.utilities.specific.ThrowableUtilities.ThrowableCatcher;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.observers.DisposableObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

public interface IBindingMethod<T> extends IHasGenericClass<T>, IHasBindingString {
	Logger LOGGER = LogManager.getLogger();

	EnumType getType();

	enum EnumType {
		SOURCE,
		DESTINATION
	}

	interface ISource<T> extends IBindingMethod<T>, ObservableSource<T> {
		static <T> DisposableObserver<T> createDelegatingObserver(Iterable<IDestination<T>> destinations) {
			return new DisposableObserver<T>() {
				@Override
				public void onNext(@NonNull T t) { destinations.forEach(d -> d.accept(t)); }

				@Override
				public void onError(@NonNull Throwable e) {
					ThrowableCatcher.log(e, LOGGER);
					dispose();
				}

				@Override
				public void onComplete() { dispose(); }
			};
		}

		@Override
		default EnumType getType() { return EnumType.SOURCE; }
	}

	interface IDestination<T> extends IBindingMethod<T>, Consumer<T> {
		@Override
		default EnumType getType() { return EnumType.DESTINATION; }
	}
}
