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

	public static final IntSupplier HASH_CODE_SUPER_METHOD_DEFAULT = () -> 1;

	@SuppressWarnings({"ObjectEquality", "UnstableApiUsage"})
	public static <T> boolean equals(T self, @Nullable Object other, boolean acceptSubclasses, @Nullable Predicate<? super Object> superMethod, Iterable<? extends Function<? super T, ?>> variables) {
		if (self == other)
			return true;
		if (acceptSubclasses) {
			if (!self.getClass().isInstance(other))
				return false;
		} else {
			if (other == null || !self.getClass().equals(other.getClass()))
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
		final int[] result = {Optional.ofNullable(superMethod).orElse(HASH_CODE_SUPER_METHOD_DEFAULT).getAsInt()};
		Streams.stream(variables).sequential()
				.map(variable -> variable.apply(self))
				.mapToInt(Objects::hashCode)
				.forEachOrdered(variableHashCode -> {
					result[0] *= 31;
					result[0] += variableHashCode;
				});
		return result[0];
	}

	public static <T> String toString(T self, @Nullable Supplier<? extends String> superMethod, Map<? extends String, ? extends Function<? super T, ?>> variables) {
		StringBuilder ret = new StringBuilder(CapacityUtilities.INITIAL_CAPACITY_LARGE);
		ret.append(self.getClass().getSimpleName());
		final boolean[] comma = {false};
		variables.forEach((key, value) -> {
			if (comma[0])
				ret.append(',');
			else
				comma[0] = true;
			ret.append(key).append('=').append(value);
		});
		if (superMethod != null)
			ret.append(superMethod.get());
		return ret.toString();
	}

	public static <T> ImmutableList<Function<? super T, ?>> extendsObjectVariables(Iterable<? extends Function<? super T, ?>> extended, Iterable<? extends Function<? super T, ?>> self) {
		return ImmutableList.copyOf(Iterables.concat(extended, self));
	}

	public static <T> @NonNls ImmutableMap<String, Function<? super T, ?>> extendsObjectVariablesMap(Collection<? extends Function<? super T, ?>> variables, Map<? extends String, ? extends Function<? super T, ?>> extended, Iterable<? extends String> self) {
		return ImmutableMap.copyOf(
				MapUtilities.zipKeysValues(Iterables.concat(extended.keySet(), self), variables));
	}
}
