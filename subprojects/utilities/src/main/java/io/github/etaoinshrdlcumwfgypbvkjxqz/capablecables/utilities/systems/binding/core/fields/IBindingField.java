package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields;

import com.google.common.reflect.TypeToken;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinding;

import javax.annotation.Nullable;
import java.util.Optional;

public interface IBindingField<T>
		extends IBinding<T>, IField<T> {
	@Override
	default EnumBindingType getBindingType() { return EnumBindingType.FIELD; }

	@SuppressWarnings("UnstableApiUsage")
	@Override
	default TypeToken<T> getTypeToken() { return getField().getTypeToken(); }

	IObservableField<T> getField();

	@Override
	default Optional<? extends T> getValue() { return getField().getValue(); }

	@Override
	default void setValue(@Nullable T value) { getField().setValue(value); }
}
