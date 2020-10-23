package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.CacheUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.InvokeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.FunctionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;

import javax.annotation.Nullable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public enum ObjectUtilities {
	;

	private static final ToIntFunction<?> HASH_CODE_SUPER_INVOKER_DEFAULT = (self) -> 1;
	private static final LoadingCache<Class<?>, BiPredicate<?, ? super Object>> EQUALS_SUPER_INVOKERS_MAP =
			CacheUtilities.newCacheBuilderNormalThreaded()
					.weakKeys()
					.initialCapacity(CapacityUtilities.getInitialCapacityEnormous())
					.build(CacheLoader.from(clazz -> {
						assert clazz != null;
						MethodHandle methodHandle;
						try {
							// TODO Java 9 - LambdaMetaFactory
							methodHandle = InvokeUtilities.getImplLookup().findSpecial(Object.class, "equals", MethodType.methodType(boolean.class, Object.class), clazz);
						} catch (NoSuchMethodException | IllegalAccessException e) {
							throw ThrowableUtilities.propagate(e);
						}
						if (Object.class.equals(InvokeUtilities.revealDeclaringClass(methodHandle))) // COMMENT 'super' calls the 'Object' impl, replace it with 'true'
							return FunctionUtilities.getAlwaysTrueBiPredicate();
						return (self, other) -> {
							try {
								return (boolean) methodHandle.invoke(self, other);
							} catch (Throwable throwable) {
								throw ThrowableUtilities.propagate(throwable);
							}
						};
					}));
	private static final LoadingCache<Class<?>, ToIntFunction<?>> HASH_CODE_SUPER_INVOKERS_MAP =
			CacheUtilities.newCacheBuilderNormalThreaded()
					.weakKeys()
					.initialCapacity(CapacityUtilities.getInitialCapacityEnormous())
					.build(CacheLoader.from(clazz -> {
						assert clazz != null;
						MethodHandle methodHandle;
						try {
							// TODO Java 9 - LambdaMetaFactory
							methodHandle = InvokeUtilities.getImplLookup().findSpecial(Object.class, "hashCode", MethodType.methodType(int.class), clazz);
						} catch (NoSuchMethodException | IllegalAccessException e) {
							throw ThrowableUtilities.propagate(e);
						}
						if (Object.class.equals(InvokeUtilities.revealDeclaringClass(methodHandle))) // COMMENT 'super' calls the 'Object' impl, replace it with 'true'
							return getHashCodeSuperInvokerDefault();
						return (self) -> {
							try {
								return (int) methodHandle.invoke(self);
							} catch (Throwable throwable) {
								throw ThrowableUtilities.propagate(throwable);
							}
						};
					}));
	private static final LoadingCache<Class<?>, Function<?, ? extends String>> TO_STRING_SUPER_INVOKERS_MAP =
			CacheUtilities.newCacheBuilderNormalThreaded()
					.weakKeys()
					.initialCapacity(CapacityUtilities.getInitialCapacityEnormous())
					.build(CacheLoader.from(clazz -> {
						assert clazz != null;
						MethodHandle methodHandle;
						try {
							// TODO Java 9 - LambdaMetaFactory
							methodHandle = InvokeUtilities.getImplLookup().findSpecial(Object.class, "toString", MethodType.methodType(String.class), clazz);
						} catch (NoSuchMethodException | IllegalAccessException e) {
							throw ThrowableUtilities.propagate(e);
						}
						return (self) -> {
							try {
								return (String) methodHandle.invoke(self);
							} catch (Throwable throwable) {
								throw ThrowableUtilities.propagate(throwable);
							}
						};
					}));

	@SuppressWarnings({"ObjectEquality", "UnstableApiUsage"})
	public static <T> boolean equalsImpl(T self,
	                                     @Nullable Object other,
	                                     Class<T> referenceClass,
	                                     boolean acceptSubclasses,
	                                     Iterable<? extends Function<? super T, ?>> variables) {
		if (self == other)
			return true;
		if (acceptSubclasses) {
			if (!referenceClass.isInstance(other))
				return false;
		} else {
			if (other == null || !referenceClass.equals(other.getClass()))
				return false;
		}
		if (!getEqualsSuperInvokersMap().getUnchecked(self.getClass()).test(CastUtilities.castUnchecked(self), other))
			return false;

		@SuppressWarnings("unchecked") T that = (T) other;
		return Streams.stream(variables).unordered()
				.allMatch(variable -> Objects.equals(variable.apply(self), variable.apply(that)));
	}

	private static LoadingCache<Class<?>, BiPredicate<?, ? super Object>> getEqualsSuperInvokersMap() {
		return EQUALS_SUPER_INVOKERS_MAP;
	}

	/**
	 * @see java.util.Arrays#hashCode(Object[])
	 */
	@SuppressWarnings({"MagicNumber", "UnstableApiUsage"})
	public static <T> int hashCodeImpl(T self, Iterable<? extends Function<? super T, ?>> variables) {
		final int[] result = {getHashCodeSuperInvokersMap().getUnchecked(self.getClass()).applyAsInt(CastUtilities.castUnchecked(self))};
		Streams.stream(variables)
				.map(variable -> variable.apply(self))
				.mapToInt(Objects::hashCode)
				.forEachOrdered(variableHashCode -> {
					result[0] *= 31;
					result[0] += variableHashCode;
				});
		return result[0];
	}

	private static LoadingCache<Class<?>, ToIntFunction<?>> getHashCodeSuperInvokersMap() {
		return HASH_CODE_SUPER_INVOKERS_MAP;
	}

	private static ToIntFunction<?> getHashCodeSuperInvokerDefault() {
		return HASH_CODE_SUPER_INVOKER_DEFAULT;
	}

	public static <T> String toStringImpl(T self, Map<? extends String, ? extends Function<? super T, ?>> variables) {
		StringBuilder ret = new StringBuilder(CapacityUtilities.getInitialCapacityLarge());
		ret.append(self.getClass().getSimpleName()).append('{');
		final boolean[] comma = {false};
		variables.forEach((key, valueFunction) -> {
			if (comma[0])
				ret.append(',');
			else
				comma[0] = true;
			ret.append(key).append('=').append(AssertionUtilities.assertNonnull(valueFunction).apply(self));
		});
		ret.append('}');
		ret.append(getToStringSuperInvokersMap().getUnchecked(self.getClass()).apply(CastUtilities.castUnchecked(self)));
		return ret.toString();
	}

	private static LoadingCache<Class<?>, Function<?, ? extends String>> getToStringSuperInvokersMap() {
		return TO_STRING_SUPER_INVOKERS_MAP;
	}
}
