package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.methods;

import com.google.common.reflect.TypeToken;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IIdentifier;
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
	private final IIdentifier bindingKey;
	private final Subject<T> notifierSubject = PublishSubject.create();

	@SuppressWarnings("UnstableApiUsage")
	private ImmutableBindingMethodSource(Class<T> type, @Nullable IIdentifier bindingKey) {
		this.typeToken = TypeToken.of(type);
		this.bindingKey = bindingKey;
	}

	public static <T> ImmutableBindingMethodSource<T> of(Class<T> type, @Nullable IIdentifier bindingKey) {
		return new ImmutableBindingMethodSource<>(type, bindingKey);
	}

	@Override
	public ObservableSource<T> getNotifier() { return getNotifierSubject(); }

	@Override
	public void invoke(T argument) { getNotifierSubject().onNext(argument); }

	protected Subject<T> getNotifierSubject() { return notifierSubject; }

	@Override
	public Optional<? extends IIdentifier> getBindingKey() { return Optional.ofNullable(bindingKey); }

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public TypeToken<T> getTypeToken() {
		return typeToken;
	}
}
