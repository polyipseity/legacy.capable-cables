package $group__.client.ui.mvvm.binding;

import $group__.client.ui.mvvm.core.binding.IObservableField;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.Subject;

public class ObservableField<T> implements IObservableField<T> {
	protected final Class<T> genericClass;
	protected final Subject<T> notifier;
	protected T value;

	public ObservableField(Class<T> genericClass, T value) {
		this.genericClass = genericClass;
		this.value = value;
		this.notifier = BehaviorSubject.createDefault(value);
	}

	@Override
	public void subscribe(@NonNull Observer<? super T> observer) { getNotifier().subscribe(observer); }	@Override
	public void setValue(T value) {
		if (!getValue().equals(value)) {
			this.value = value;
			getNotifier().onNext(value);
		}
	}

	@Override
	public Class<T> getGenericClass() { return genericClass; }	protected Subject<T> getNotifier() { return notifier; }

	@Override
	public T getValue() { return value; }




}
