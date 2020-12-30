package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.fields;

import com.google.common.reflect.TypeToken;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.IBinding;

public interface IBindingField<T>
		extends IBinding<T>, IField<T> {
	@Override
	default EnumBindingType getBindingType() { return EnumBindingType.FIELD; }

	@SuppressWarnings("UnstableApiUsage")
	@Override
	default TypeToken<T> getTypeToken() { return getField().getTypeToken(); }

	IObservableField<T> getField();

	@Override
	default T getValue() { return getField().getValue(); }

	@Override
	default void setValue(T value) { getField().setValue(value); }
}
