package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.binding;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.fields.ImmutableBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.fields.MemoryObservableField;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public interface IUIPropertyMappingValue {
	static <T> IBindingField<T> createBindingField(Class<T> clazz,
	                                               Supplier<@Nonnull ? extends T> defaultValueSupplier,
	                                               @Nullable IUIPropertyMappingValue mapping) {
		return createBindingField(clazz, defaultValueSupplier, mapping, clazz, Function.identity());
	}

	static <T, T0> IBindingField<T> createBindingField(Class<T> clazz,
	                                                   Supplier<@Nonnull ? extends T> defaultValueSupplier,
	                                                   @Nullable IUIPropertyMappingValue mapping,
	                                                   Class<T0> mappingClazz,
	                                                   Function<@Nonnull ? super T0, ? extends T> mapper) {
		Optional<IUIPropertyMappingValue> mapping1 = Optional.ofNullable(mapping);
		return ImmutableBindingField.of(
				mapping1.flatMap(IUIPropertyMappingValue::getBindingKey)
						.orElse(null),
				new MemoryObservableField<>(clazz,
						mapping1.flatMap(IUIPropertyMappingValue::getDefaultValue)
								.map(mappingClazz::cast)
								.map(mapper)
								.map(clazz::cast)
								.orElseGet(defaultValueSupplier)
				)
		);
	}

	Optional<? extends IIdentifier> getBindingKey();

	Optional<?> getDefaultValue();
}
