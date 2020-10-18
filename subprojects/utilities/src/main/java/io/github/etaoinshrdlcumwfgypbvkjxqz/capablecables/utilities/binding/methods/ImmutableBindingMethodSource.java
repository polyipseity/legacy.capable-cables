package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.methods;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.methods.IBindingMethodSource;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.IHasGenericClass;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

import javax.annotation.Nullable;
import java.util.Optional;

public final class ImmutableBindingMethodSource<T>
		extends IHasGenericClass.Impl<T>
		implements IBindingMethodSource<T> {
	@Nullable
	private final INamespacePrefixedString bindingKey;
	private final Subject<T> notifierSubject = PublishSubject.create();

	public ImmutableBindingMethodSource(Class<T> genericClass, @Nullable INamespacePrefixedString bindingKey) {
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
