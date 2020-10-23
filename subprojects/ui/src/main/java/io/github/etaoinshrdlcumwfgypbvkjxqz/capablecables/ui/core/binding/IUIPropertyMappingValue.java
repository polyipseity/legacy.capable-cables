package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ConstantValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.fields.ImmutableBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.fields.MemoryObservableField;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

public interface IUIPropertyMappingValue {
	static <T> IBindingField<T> createBindingField(Class<T> clazz,
	                                               boolean nullable,
	                                               @Nullable T defaultValue,
	                                               @Nullable IUIPropertyMappingValue mapping) {
		return createBindingField(clazz, nullable, ConstantValue.of(defaultValue), mapping);
	}

	static <T> IBindingField<T> createBindingField(Class<T> clazz,
	                                               boolean nullable,
	                                               Supplier<? extends T> defaultValue,
	                                               @Nullable IUIPropertyMappingValue mapping) {
		Optional<IUIPropertyMappingValue> m = Optional.ofNullable(mapping);
		return new ImmutableBindingField<>(
				Optional.ofNullable(mapping)
						.flatMap(IUIPropertyMappingValue::getBindingKey)
						.orElse(null),
				new MemoryObservableField<>(clazz,
						m.isPresent()
								? m // COMMENT has mapping
								.flatMap(IUIPropertyMappingValue::getDefaultValue)
								.map(clazz::cast)
								.orElseGet(nullable ? ConstantValue.getEmpty() : defaultValue)
								: defaultValue.get()));
	}

	Optional<?> getDefaultValue();

	Optional<? extends INamespacePrefixedString> getBindingKey();
}
