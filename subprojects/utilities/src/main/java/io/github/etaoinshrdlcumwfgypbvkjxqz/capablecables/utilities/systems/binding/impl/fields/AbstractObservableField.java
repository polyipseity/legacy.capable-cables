package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.fields;

import com.google.common.reflect.TypeToken;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IObservableField;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

public abstract class AbstractObservableField<T>
		implements IObservableField<T> {
	@SuppressWarnings("UnstableApiUsage")
	private final TypeToken<T> typeToken;
	private final Subject<T> notifierSubject = PublishSubject.create();

	@SuppressWarnings("UnstableApiUsage")
	public AbstractObservableField(Class<T> type) {
		this.typeToken = TypeToken.of(type);
	}

	@Override
	public ObservableSource<? extends T> getNotifier() {
		return getNotifierSubject();
	}

	@Override
	@SuppressWarnings("UnstableApiUsage")
	public TypeToken<T> getTypeToken() {
		return typeToken;
	}

	protected Subject<T> getNotifierSubject() {
		return notifierSubject;
	}
}
