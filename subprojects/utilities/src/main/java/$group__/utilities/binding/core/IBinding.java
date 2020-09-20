package $group__.utilities.binding.core;

import $group__.utilities.binding.bindings.FieldBindings;
import $group__.utilities.binding.bindings.MethodBindings;
import $group__.utilities.binding.core.bindings.IBindings;
import $group__.utilities.binding.core.traits.IHasBindingKey;
import $group__.utilities.interfaces.IHasGenericClass;
import $group__.utilities.structures.INamespacePrefixedString;
import com.google.common.cache.Cache;

import java.util.function.Function;
import java.util.function.Supplier;

public interface IBinding<T> extends IHasGenericClass<T>, IHasBindingKey {
	EnumBindingType getBindingType();

	enum EnumBindingType {
		FIELD {
			@Override
			public IBindings<?> createBindings(INamespacePrefixedString bindingKey,
			                                   Supplier<? extends Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<?, ?>>>> transformersSupplier) {
				return new FieldBindings(bindingKey, transformersSupplier);
			}
		},
		METHOD {
			@Override
			public IBindings<?> createBindings(INamespacePrefixedString bindingKey,
			                                   Supplier<? extends Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<?, ?>>>> transformersSupplier) {
				return new MethodBindings(bindingKey, transformersSupplier);
			}
		},
		;

		public abstract IBindings<?> createBindings(INamespacePrefixedString bindingKey,
		                                            Supplier<? extends Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<?, ?>>>> transformersSupplier);
	}
}
