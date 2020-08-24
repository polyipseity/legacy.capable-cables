package $group__.ui.mvvm.binding;

import $group__.ui.core.mvvm.binding.IBindingMethod;
import $group__.utilities.interfaces.IHasGenericClass;
import $group__.utilities.interfaces.INamespacePrefixedString;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

import javax.annotation.Nullable;
import java.util.Optional;

public class BindingMethodSource<T>
		extends IHasGenericClass.Impl<T>
		implements IBindingMethod.ISource<T> {
	@Nullable
	protected final INamespacePrefixedString bindingKey;
	protected final Subject<T> notifierSubject = PublishSubject.create();

	public BindingMethodSource(Class<T> genericClass, @Nullable INamespacePrefixedString bindingKey) {
		super(genericClass);
		this.bindingKey = bindingKey;
	}

	@Override
	public ObservableSource<T> getNotifier() { return getNotifierSubject(); }

	@Override
	public void invoke(T argument) { getNotifierSubject().onNext(argument); }

	protected Subject<T> getNotifierSubject() { return notifierSubject; }

	@Override
	public Optional<INamespacePrefixedString> getBindingKey() { return Optional.ofNullable(bindingKey); }
}
