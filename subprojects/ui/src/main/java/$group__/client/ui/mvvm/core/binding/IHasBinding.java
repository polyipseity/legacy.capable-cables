package $group__.client.ui.mvvm.core.binding;

import io.reactivex.rxjava3.core.ObservableSource;

public interface IHasBinding extends ObservableSource<IBinderAction> {
	Iterable<IBindingField<?>> getBindingFields();

	Iterable<IBindingMethod<?>> getBindingMethods();
}
