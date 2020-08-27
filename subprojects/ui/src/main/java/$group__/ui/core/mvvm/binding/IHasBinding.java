package $group__.ui.core.mvvm.binding;

import $group__.ui.core.mvvm.structures.IUIPropertyMappingValue;
import $group__.ui.mvvm.binding.BindingField;
import $group__.ui.mvvm.binding.ObservableField;
import $group__.ui.utilities.BindingUtilities;
import $group__.ui.utilities.BindingUtilities.EnumOptions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import io.reactivex.rxjava3.core.ObservableSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Node;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public interface IHasBinding {
	Logger LOGGER = LogManager.getLogger();

	static <T> IBindingField<T> createBindingField(Class<T> clazz,
	                                               @Nullable IUIPropertyMappingValue mapping,
	                                               Function<? super Node, ? extends Optional<? extends T>> deserializer, T defaultValue) {
		return IHasBinding.<T>createBindingField(clazz, mapping, deserializer, () -> defaultValue);
	}

	static <T> IBindingField<T> createBindingField(Class<T> clazz,
	                                               @Nullable IUIPropertyMappingValue mapping,
	                                               Function<? super Node, ? extends Optional<? extends T>> deserializer, Supplier<T> defaultValue) {
		return new BindingField<>(
				Optional.ofNullable(mapping)
						.flatMap(IUIPropertyMappingValue::getBindingKey)
						.orElse(null),
				new ObservableField<>(clazz,
						Optional.ofNullable(mapping)
								.flatMap(IUIPropertyMappingValue::getDefaultValue)
								.<T>flatMap(n -> deserializer.apply(n).map(Function.identity()))
								.orElseGet(defaultValue)));
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
