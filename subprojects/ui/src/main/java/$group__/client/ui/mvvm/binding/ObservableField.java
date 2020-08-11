package $group__.client.ui.mvvm.binding;

import $group__.client.ui.mvvm.core.binding.IObservableField;
import $group__.utilities.interfaces.IHasGenericClass;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.Subject;

public class ObservableField<T>
		extends IHasGenericClass.Impl<T>
		implements IObservableField<T> {
	protected final Subject<T> notifierSubject;
	protected T value;

	public ObservableField(Class<T> genericClass, T value) {
		super(genericClass);
		this.value = value;
		this.notifierSubject = BehaviorSubject.createDefault(value);
	}

	@Override
	public ObservableSource<T> getNotifier() { return getNotifierSubject(); }

	protected Subject<T> getNotifierSubject() { return notifierSubject; }

	@Override
	public void setValue(T value) {
		if (!getValue().equals(value)) {
			this.value = value;
			getNotifierSubject().onNext(value);
		}
	}

	@Override
	public T getValue() { return value; }
}
