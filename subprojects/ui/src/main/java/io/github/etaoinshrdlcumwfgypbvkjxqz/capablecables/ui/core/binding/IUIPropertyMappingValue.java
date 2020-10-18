package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.fields.ImmutableBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.fields.MemoryObservableField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.ConstantSupplier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

public interface IUIPropertyMappingValue {
	static <T> IBindingField<T> createBindingField(Class<T> clazz,
	                                               boolean nullable,
	                                               @Nullable T defaultValue,
	                                               @Nullable IUIPropertyMappingValue mapping) {
		return createBindingField(clazz, nullable, ConstantSupplier.ofNullable(defaultValue), mapping);
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
								.orElseGet(nullable ? ConstantSupplier.empty() : defaultValue)
								: defaultValue.get()));
	}

	Optional<?> getDefaultValue();

	Optional<? extends INamespacePrefixedString> getBindingKey();
}
