package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ConstantValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.fields.ImmutableBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.fields.MemoryObservableField;

import java.util.Optional;
import java.util.function.Supplier;

public interface IUIPropertyMappingValue {
	static <T> IBindingField<T> createBindingField(Class<T> clazz,
	                                               T defaultValue,
	                                               @Nullable IUIPropertyMappingValue mapping) {
		return createBindingField(clazz, ConstantValue.of(defaultValue), mapping);
	}

	static <T> IBindingField<T> createBindingField(Class<T> clazz,
	                                               Supplier<@Nonnull ? extends T> defaultValueSupplier,
	                                               @Nullable IUIPropertyMappingValue mapping) {
		return new ImmutableBindingField<>(
				Optional.ofNullable(mapping)
						.flatMap(IUIPropertyMappingValue::getBindingKey)
						.orElse(null),
				new MemoryObservableField<>(clazz,
						Optional.ofNullable(mapping)
								.flatMap(IUIPropertyMappingValue::getDefaultValue)
								.map(clazz::cast)
								.orElseGet(defaultValueSupplier)
				)
		);
	}

	Optional<? extends INamespacePrefixedString> getBindingKey();

	Optional<?> getDefaultValue();
}
