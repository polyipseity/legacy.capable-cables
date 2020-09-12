package $group__.utilities;

import $group__.utilities.collections.MapUtilities;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public enum ObjectUtilities {
	;

	public static final IntSupplier HASH_CODE_SUPER_METHOD_DEFAULT = () -> 1;

	@SuppressWarnings("ObjectEquality")
	public static <T> boolean equals(T self, @Nullable Object other, boolean acceptSubclasses, @Nullable Function<? super Object, ? extends Boolean> superMethod, Iterable<? extends Function<? super T, ?>> variables) {
		if (self == other)
			return true;
		if (acceptSubclasses) {
			if (!self.getClass().isInstance(other))
				return false;
		} else {
			if (other == null || !self.getClass().equals(other.getClass()))
				return false;
		}
		if (superMethod != null && !superMethod.apply(other))
			return false;

		@SuppressWarnings("unchecked") T that = (T) other;
		for (Function<? super T, ?> variable : variables) {
			if (!Objects.equals(variable.apply(self), variable.apply(that)))
				return false;
		}
		return true;
	}

	/**
	 * @see java.util.Arrays#hashCode(Object[])
	 */
	@SuppressWarnings("MagicNumber")
	public static <T> int hashCode(T self, @Nullable IntSupplier superMethod, Iterable<? extends Function<? super T, ?>> variables) {
		int result = (superMethod == null ? HASH_CODE_SUPER_METHOD_DEFAULT : superMethod).getAsInt();
		for (Function<? super T, ?> variable : variables) {
			result = 31 * result + Objects.hashCode(variable.apply(self));
		}
		return result;
	}

	public static <T> String toString(T self, @Nullable Supplier<? extends String> superMethod, Map<? extends String, ? extends Function<? super T, ?>> variables) {
		StringBuilder ret = new StringBuilder(CapacityUtilities.INITIAL_CAPACITY_LARGE);
		ret.append(self.getClass().getSimpleName());
		boolean comma = false;
		for (Map.Entry<? extends String, ? extends Function<? super T, ?>> entry : variables.entrySet()) {
			if (comma)
				ret.append(',');
			else
				comma = true;
			ret.append(entry.getKey()).append('=').append(entry.getValue());
		}
		if (superMethod != null)
			ret.append(superMethod.get());
		return ret.toString();
	}

	public static <T> ImmutableList<Function<? super T, ?>> extendsObjectVariables(Iterable<? extends Function<? super T, ?>> extended, Iterable<? extends Function<? super T, ?>> self) { return ImmutableList.copyOf(Iterables.concat(extended, self)); }

	public static <T> ImmutableMap<String, Function<? super T, ?>> extendsObjectVariablesMap(Collection<? extends Function<? super T, ?>> variables, Map<? extends String, ? extends Function<? super T, ?>> extended, Iterable<? extends String> self) {
		return ImmutableMap.copyOf(
				MapUtilities.stitchKeysValues(variables.size(), Iterables.concat(extended.keySet(), self), variables));
	}
}
