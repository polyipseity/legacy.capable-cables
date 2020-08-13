package $group__.client.ui.mvvm.core.binding;

import $group__.client.ui.mvvm.binding.BindingField;
import $group__.client.ui.mvvm.binding.ObservableField;
import $group__.client.ui.mvvm.core.structures.IUIPropertyMappingValue;
import $group__.client.ui.utilities.BindingUtilities;
import io.reactivex.rxjava3.core.ObservableSource;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IHasBinding {
	Logger LOGGER = LogManager.getLogger();

	static <T> IBindingField<T> createBindingField(Class<T> clazz,
	                                               @Nullable IUIPropertyMappingValue mapping,
	                                               Function<? super String, ? extends T> converter, T defaultValue) {
		return new BindingField<>(Optional.ofNullable(mapping).flatMap(IUIPropertyMappingValue::getDefaultValue).map(ResourceLocation::new).orElse(null),
				new ObservableField<>(clazz, Optional.ofNullable(mapping).flatMap(IUIPropertyMappingValue::getBindingString).<T>map(converter).orElse(defaultValue)));
	}

	static <T> IBindingField<T> createBindingField(Class<T> clazz,
	                                               @Nullable IUIPropertyMappingValue mapping,
	                                               Function<? super String, ? extends T> converter, Supplier<T> defaultValue) {
		return new BindingField<>(Optional.ofNullable(mapping).flatMap(IUIPropertyMappingValue::getDefaultValue).map(ResourceLocation::new).orElse(null),
				new ObservableField<>(clazz, Optional.ofNullable(mapping).flatMap(IUIPropertyMappingValue::getBindingString).<T>map(converter).orElseGet(defaultValue)));
	}

	default Iterable<IBindingField<?>> getBindingFields() { return BindingUtilities.getBindingFields(this); }

	default Iterable<IBindingMethod<?>> getBindingMethods() { return BindingUtilities.getBindingMethods(this); }

	ObservableSource<IBinderAction> getBinderNotifier();
}
