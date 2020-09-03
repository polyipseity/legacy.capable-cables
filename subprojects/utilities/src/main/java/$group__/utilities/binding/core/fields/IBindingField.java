package $group__.utilities.binding.core.fields;

import $group__.utilities.binding.core.IBinding;

import javax.annotation.Nullable;
import java.util.Optional;

public interface IBindingField<T>
		extends IBinding<T>, IField<T> {
	@Override
	default EnumBindingType getBindingType() { return EnumBindingType.FIELD; }

	@Override
	default Class<T> getGenericClass() { return getField().getGenericClass(); }

	IObservableField<T> getField();

	@Override
	default Optional<? extends T> getValue() { return getField().getValue(); }

	@Override
	default void setValue(@Nullable T value) { getField().setValue(value); }
}
