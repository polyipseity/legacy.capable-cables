package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.bindings.FieldBindings;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.bindings.MethodBindings;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.bindings.IBindings;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.traits.IHasBindingKey;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.IHasGenericClass;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.INamespacePrefixedString;
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
