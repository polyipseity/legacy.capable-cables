package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.fields;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.IHasGenericClass;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ConstantValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IObservableField;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

public class MemoryObservableField<T>
		extends IHasGenericClass.Impl<T>
		implements IObservableField<T> {
	private final Subject<IValue<T>> notifierSubject = PublishSubject.create();
	@Nullable
	private T value;

	public MemoryObservableField(Class<T> genericClass, @Nullable T value) {
		super(genericClass);
		this.value = value;
	}

	@Override
	public ObservableSource<? extends IValue<T>> getNotifier() { return getNotifierSubject(); }

	protected Subject<IValue<T>> getNotifierSubject() { return notifierSubject; }

	@Override
	public void setValue(@Nullable T value) {
		if (!Objects.equals(getValue().orElse(null), value)) {
			this.value = value;
			getNotifierSubject().onNext(ConstantValue.of(value));
		}
	}

	@Override
	public Optional<? extends T> getValue() { return Optional.ofNullable(value); }
}
