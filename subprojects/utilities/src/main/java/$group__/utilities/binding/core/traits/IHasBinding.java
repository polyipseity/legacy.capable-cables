package $group__.utilities.binding.core.traits;

import $group__.ui.core.mvvm.structures.IUIPropertyMappingValue;
import $group__.ui.mvvm.binding.BindingField;
import $group__.ui.mvvm.binding.ObservableField;
import $group__.ui.utilities.BindingUtilities;
import $group__.ui.utilities.BindingUtilities.EnumOptions;
import $group__.utilities.functions.ConstantSupplier;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import io.reactivex.rxjava3.core.ObservableSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Node;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public interface IHasBinding {
	Logger LOGGER = LogManager.getLogger();

	static <T> IBindingField<T> createBindingField(Class<T> clazz,
	                                               boolean nullable,
	                                               T defaultValue,
	                                               @Nullable IUIPropertyMappingValue mapping,
	                                               BiFunction<? super Node, ? super Boolean, ? extends Optional<? extends T>> deserializer) {
		return IHasBinding.createBindingField(clazz, nullable, ConstantSupplier.of(defaultValue), mapping, deserializer);
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

	default Iterable<? extends IBindingField<?>> getBindingFields() { return BindingUtilities.getBindingFields(this, EnumSet.of(EnumOptions.SELF, EnumOptions.VARIABLES)); }

	default Iterable<? extends IBindingMethod<?>> getBindingMethods() { return BindingUtilities.getBindingMethods(this, EnumSet.of(EnumOptions.SELF, EnumOptions.VARIABLES)); }

	@SuppressWarnings("UnstableApiUsage")
	default Iterable<? extends ObservableSource<IBinderAction>> getBinderNotifiers() {
		return Streams.stream(BindingUtilities.getHasBindingsVariables(this)).unordered()
				.flatMap(v -> Streams.stream(v.getBinderNotifiers()))
				.collect(ImmutableSet.toImmutableSet());
	}
}
