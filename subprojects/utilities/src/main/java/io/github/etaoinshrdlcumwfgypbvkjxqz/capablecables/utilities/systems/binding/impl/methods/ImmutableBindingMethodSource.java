package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.methods;

import com.google.common.reflect.TypeToken;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.methods.IBindingMethodSource;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

import java.util.Optional;

public final class ImmutableBindingMethodSource<T>
		implements IBindingMethodSource<T> {
	@SuppressWarnings("UnstableApiUsage")
	private final TypeToken<T> typeToken;
	@Nullable
	private final INamespacePrefixedString bindingKey;
	private final Subject<T> notifierSubject = PublishSubject.create();

	@SuppressWarnings("UnstableApiUsage")
	public ImmutableBindingMethodSource(Class<T> type, @Nullable INamespacePrefixedString bindingKey) {
		this.typeToken = TypeToken.of(type);
		this.bindingKey = bindingKey;
	}

	@Override
	public ObservableSource<T> getNotifier() { return getNotifierSubject(); }

	@Override
	public void invoke(T argument) { getNotifierSubject().onNext(argument); }

	protected Subject<T> getNotifierSubject() { return notifierSubject; }

	@Override
	public Optional<? extends INamespacePrefixedString> getBindingKey() { return Optional.ofNullable(bindingKey); }

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public TypeToken<T> getTypeToken() {
		return typeToken;
	}
}
