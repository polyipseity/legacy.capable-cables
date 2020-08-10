package $group__.client.ui.mvvm.core.binding;

import io.reactivex.rxjava3.core.ObservableSource;

public interface IHasBinding extends ObservableSource<IBinderAction> {
	Logger LOGGER = LogManager.getLogger();

	Iterable<IBindingField<?>> getBindingFields();

	Iterable<IBindingMethod<?>> getBindingMethods();

	static <T> IBindingField<T> createBindingField(Class<T> clazz,
	                                               @Nullable IUIPropertyMappingValue mapping,
	                                               Function<? super String, ? extends T> converter, T defaultValue) {
		return new BindingField<>(Optional.ofNullable(mapping).flatMap(IUIPropertyMappingValue::getDefaultValue).orElse(null),
				new ObservableField<>(clazz, Optional.ofNullable(mapping).flatMap(IUIPropertyMappingValue::getBindingString).<T>map(converter).orElse(defaultValue)));
	}

	static <T> IBindingField<T> createBindingField(Class<T> clazz,
	                                               @Nullable IUIPropertyMappingValue mapping,
	                                               Function<? super String, ? extends T> converter, Supplier<T> defaultValue) {
		return new BindingField<>(Optional.ofNullable(mapping).flatMap(IUIPropertyMappingValue::getDefaultValue).orElse(null),
				new ObservableField<>(clazz, Optional.ofNullable(mapping).flatMap(IUIPropertyMappingValue::getBindingString).<T>map(converter).orElseGet(defaultValue)));
	}

	Map<String, IUIPropertyMappingValue> getPropertyMappingView();
}
