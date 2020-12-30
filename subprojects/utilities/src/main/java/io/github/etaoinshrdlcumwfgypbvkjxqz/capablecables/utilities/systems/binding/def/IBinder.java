package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.DynamicUtilities;
import net.jodah.typetools.TypeResolver;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * Not thread-safe.
 */
@SuppressWarnings("UnusedReturnValue")
public interface IBinder {
	static Optional<Class<?>>[] resolveFunctionTypes(Function<?, ?> transformer) {
		return DynamicUtilities.Extensions.wrapTypeResolverResults(TypeResolver.resolveRawArguments(Function.class, transformer.getClass()));
	}

	boolean bind(Iterator<? extends IBinding<?>> bindings)
			throws NoSuchBindingTransformerException;

	boolean unbind(Iterator<? extends IBinding<?>> bindings);

	<T, R> Optional<? extends Function<@Nonnull T, @Nonnull R>> addTransformer(IBinding.EnumBindingType type, Function<@Nonnull T, @Nonnull R> transformer);

	<T, R> Optional<? extends Function<@Nonnull T, @Nonnull R>> removeTransformer(IBinding.EnumBindingType type, Class<T> from, Class<R> to);

	default boolean unbindAll() { return unbindAll(EnumSet.allOf(IBinding.EnumBindingType.class)); }

	boolean unbindAll(Set<IBinding.EnumBindingType> types);
}
