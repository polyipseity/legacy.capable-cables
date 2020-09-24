package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.DynamicUtilities;
import net.jodah.typetools.TypeResolver;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * Not thread-safe.
 */
@SuppressWarnings("UnusedReturnValue")
public interface IBinder {
	static Optional<Class<?>>[] resolveFunctionTypes(Function<?, ?> transformer) { return DynamicUtilities.Extensions.wrapTypeResolverResults(TypeResolver.resolveRawArguments(Function.class, transformer.getClass())); }

	boolean bind(Iterable<? extends IBinding<?>> bindings)
			throws NoSuchBindingTransformerException;

	boolean unbind(Iterable<? extends IBinding<?>> bindings);

	<T, R> Optional<? extends Function<T, R>> addTransformer(IBinding.EnumBindingType type, Function<T, R> transformer);

	<T, R> Optional<? extends Function<T, R>> removeTransformer(IBinding.EnumBindingType type, Class<T> from, Class<R> to);

	default boolean unbindAll() { return unbindAll(EnumSet.allOf(IBinding.EnumBindingType.class)); }

	boolean unbindAll(Set<IBinding.EnumBindingType> types);
}
