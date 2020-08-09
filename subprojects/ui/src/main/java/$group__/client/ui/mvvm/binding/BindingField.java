package $group__.client.ui.mvvm.binding;

import $group__.client.ui.mvvm.core.binding.IBindingField;
import $group__.client.ui.mvvm.core.binding.IObservableField;

import javax.annotation.Nullable;
import java.util.Optional;

public class BindingField<T> implements IBindingField<T> {
	@Nullable
	protected final String string;
	protected final IObservableField<T> field;

	public BindingField(@Nullable String string, IObservableField<T> field) {
		this.string = string;
		this.field = field;
	}

	@Override
	public IObservableField<T> getField() { return field; }

	@Override
	public Optional<String> getString() { return Optional.ofNullable(string); }
}
