package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def;

import com.google.common.cache.Cache;
import com.google.common.reflect.TypeToken;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.ITypeCapture;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.bindings.IBindings;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.traits.IHasBindingKey;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.bindings.FieldBindings;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.bindings.MethodBindings;

import java.util.function.Function;
import java.util.function.Supplier;

public interface IBinding<T> extends ITypeCapture, IHasBindingKey {
	EnumBindingType getBindingType();

	@SuppressWarnings("UnstableApiUsage")
	@Override
	TypeToken<T> getTypeToken();

	enum EnumBindingType {
		FIELD {
			@Override
			public IBindings<?> createBindings(IIdentifier bindingKey,
			                                   Supplier<@Nonnull ? extends Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<@Nonnull ?, @Nonnull ?>>>> transformersSupplier) {
				return new FieldBindings(bindingKey, transformersSupplier);
			}
		},
		METHOD {
			@Override
			public IBindings<?> createBindings(IIdentifier bindingKey,
			                                   Supplier<@Nonnull ? extends Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<@Nonnull ?, @Nonnull ?>>>> transformersSupplier) {
				return new MethodBindings(bindingKey, transformersSupplier);
			}
		},
		;

		public abstract IBindings<?> createBindings(IIdentifier bindingKey,
		                                            Supplier<@Nonnull ? extends Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<@Nonnull ?, @Nonnull ?>>>> transformersSupplier);
	}
}
