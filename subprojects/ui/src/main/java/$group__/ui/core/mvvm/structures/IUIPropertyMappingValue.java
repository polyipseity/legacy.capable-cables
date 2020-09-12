package $group__.ui.core.mvvm.structures;

import $group__.utilities.binding.core.fields.IBindingField;
import $group__.utilities.binding.fields.BindingField;
import $group__.utilities.binding.fields.ObservableField;
import $group__.utilities.functions.ConstantSupplier;
import $group__.utilities.interfaces.INamespacePrefixedString;

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
		return new BindingField<>(
				Optional.ofNullable(mapping)
						.flatMap(IUIPropertyMappingValue::getBindingKey)
						.orElse(null),
				new ObservableField<>(clazz,
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
