package $group__.ui.mvvm.binding;

import $group__.ui.core.mvvm.binding.IBindingField;
import $group__.ui.core.mvvm.binding.IObservableField;
import $group__.utilities.interfaces.INamespacePrefixedString;

import javax.annotation.Nullable;
import java.util.Optional;

public class BindingField<T> implements IBindingField<T> {
	@Nullable
	protected final INamespacePrefixedString bindingKey;
	protected final IObservableField<T> field;

	public BindingField(@Nullable INamespacePrefixedString bindingKey, IObservableField<T> field) {
		this.bindingKey = bindingKey;
		this.field = field;
	}

	@Override
	public IObservableField<T> getField() { return field; }

	@Override
	public Optional<INamespacePrefixedString> getBindingKey() { return Optional.ofNullable(bindingKey); }
}
