package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapUtilities;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

public enum ObjectUtilities {
	;

	private static final IntSupplier HASH_CODE_SUPER_METHOD_DEFAULT = () -> 1;

	@SuppressWarnings("unchecked")
	public static <T> boolean equals(T self, @Nullable Object other, boolean acceptSubclasses, @Nullable Predicate<? super Object> superMethod, Iterable<? extends Function<? super T, ?>> variables) {
		return equals(self, ((Class<T>) self.getClass()), other, acceptSubclasses, superMethod, variables);
	}

	@SuppressWarnings({"ObjectEquality", "UnstableApiUsage"})
	public static <T> boolean equals(T self, Class<T> referenceClazz, @Nullable Object other, boolean acceptSubclasses, @Nullable Predicate<? super Object> superMethod, Iterable<? extends Function<? super T, ?>> variables) {
		if (self == other)
			return true;
		if (acceptSubclasses) {
			if (!referenceClazz.isInstance(other))
				return false;
		} else {
			if (other == null || !referenceClazz.equals(other.getClass()))
				return false;
		}
		if (superMethod != null && !superMethod.test(other))
			return false;

		@SuppressWarnings("unchecked") T that = (T) other;
		return Streams.stream(variables).unordered()
				.allMatch(variable -> Objects.equals(variable.apply(self), variable.apply(that)));
	}

	/**
	 * @see java.util.Arrays#hashCode(Object[])
	 */
	@SuppressWarnings({"MagicNumber", "UnstableApiUsage"})
	public static <T> int hashCode(T self, @Nullable IntSupplier superMethod, Iterable<? extends Function<? super T, ?>> variables) {
		final int[] result = {Optional.ofNullable(superMethod).orElse(getHashCodeSuperMethodDefault()).getAsInt()};
		Streams.stream(variables).sequential()
				.map(variable -> variable.apply(self))
				.mapToInt(Objects::hashCode)
				.forEachOrdered(variableHashCode -> {
					result[0] *= 31;
					result[0] += variableHashCode;
				});
		return result[0];
	}

	public static IntSupplier getHashCodeSuperMethodDefault() {
		return HASH_CODE_SUPER_METHOD_DEFAULT;
	}

	public static <T> ImmutableList<Function<? super T, ?>> extendsObjectVariables(Iterable<? extends Function<? super T, ?>> extended, Iterable<? extends Function<? super T, ?>> self) {
		return ImmutableList.copyOf(Iterables.concat(extended, self));
	}

	public static <T> @NonNls ImmutableMap<String, Function<? super T, ?>> extendsObjectVariablesMap(Collection<? extends Function<? super T, ?>> variables, Map<? extends String, ? extends Function<? super T, ?>> extended, Iterable<? extends String> self) {
		return ImmutableMap.copyOf(
				MapUtilities.zipKeysValues(Iterables.concat(extended.keySet(), self), variables));
	}

	public static <T> String toString(T self, @Nullable Supplier<? extends String> superMethod, Map<? extends String, ? extends Function<? super T, ?>> variables) {
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
		if (superMethod != null)
			ret.append(superMethod.get());
		return ret.toString();
	}
}
