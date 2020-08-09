package $group__.client.ui.mvvm.core.binding;

import $group__.utilities.interfaces.IHasGenericClass;
import io.reactivex.rxjava3.core.ObservableSource;

public interface IObservableField<T> extends ObservableSource<T>, IHasGenericClass<T> {
	T getValue();

	void setValue(T value);
}
