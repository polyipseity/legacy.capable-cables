package $group__.utilities.binding.methods;

import $group__.utilities.binding.core.methods.IBindingMethodSource;
import $group__.utilities.interfaces.IHasGenericClass;
import $group__.utilities.structures.INamespacePrefixedString;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

import javax.annotation.Nullable;
import java.util.Optional;

public class BindingMethodSource<T>
		extends IHasGenericClass.Impl<T>
		implements IBindingMethodSource<T> {
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
	public Optional<? extends INamespacePrefixedString> getBindingKey() { return Optional.ofNullable(bindingKey); }
}
