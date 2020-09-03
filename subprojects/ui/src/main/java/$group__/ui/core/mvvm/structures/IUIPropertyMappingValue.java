package $group__.ui.core.mvvm.structures;

import $group__.utilities.binding.core.fields.IBindingField;
import $group__.utilities.binding.fields.BindingField;
import $group__.utilities.binding.fields.ObservableField;
import $group__.utilities.functions.ConstantSupplier;
import $group__.utilities.interfaces.INamespacePrefixedString;
import org.w3c.dom.Node;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public interface IUIPropertyMappingValue {
	static <T> IBindingField<T> createBindingField(Class<T> clazz,
	                                               boolean nullable,
	                                               T defaultValue,
	                                               @Nullable IUIPropertyMappingValue mapping,
	                                               BiFunction<? super Node, ? super Boolean, ? extends Optional<? extends T>> deserializer) {
		return createBindingField(clazz, nullable, ConstantSupplier.of(defaultValue), mapping, deserializer);
	}

	static <T> IBindingField<T> createBindingField(Class<T> clazz,
	                                               boolean nullable,
	                                               Supplier<? extends T> defaultValue,
	                                               @Nullable IUIPropertyMappingValue mapping,
	                                               BiFunction<? super Node, ? super Boolean, ? extends Optional<? extends T>> deserializer) {
		Optional<? extends Node> node = Optional.ofNullable(mapping)
				.flatMap(IUIPropertyMappingValue::getDefaultValue);
		return new BindingField<>(
				Optional.ofNullable(mapping)
						.flatMap(IUIPropertyMappingValue::getBindingKey)
						.orElse(null),
				new ObservableField<>(clazz,
						node.isPresent()
								? node.<T>flatMap(n2 -> deserializer.apply(n2, nullable).map(Function.identity()))
								.orElseGet(nullable ? ConstantSupplier.empty() : defaultValue) // COMMENT has node
								: defaultValue.get()));
	}

	Optional<? extends Node> getDefaultValue();

	Optional<? extends INamespacePrefixedString> getBindingKey();
}
