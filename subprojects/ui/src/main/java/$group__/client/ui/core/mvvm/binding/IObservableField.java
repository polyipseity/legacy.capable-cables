package $group__.client.ui.core.mvvm.binding;

import $group__.utilities.interfaces.IHasGenericClass;
import io.reactivex.rxjava3.core.ObservableSource;

public interface IObservableField<T> extends IField<T>, IHasGenericClass<T> {
	ObservableSource<T> getNotifier();
}
