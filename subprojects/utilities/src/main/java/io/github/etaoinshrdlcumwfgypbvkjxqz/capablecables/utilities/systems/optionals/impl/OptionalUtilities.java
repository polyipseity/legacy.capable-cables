package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.optionals.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

public enum OptionalUtilities {
	;

	@SuppressWarnings("unchecked")
	public static <T> Optional<T> upcast(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<? extends T> instance) {
		/* COMMENT
		'Optional' is immutable,
		meaning that upcasting 'Optional' will not provide a way to make the value inside not of the original 'Optional' type.
		This should mean the upcast is safe.
		 */
		return (Optional<T>) instance;
	}

	@SuppressWarnings({"ConditionalCanBeOptional", "AutoUnboxing"})
	public static OptionalInt ofInt(@Nullable Integer value) {
		return value == null ? OptionalInt.empty() : OptionalInt.of(value);
	}

	@SuppressWarnings({"ConditionalCanBeOptional", "AutoUnboxing"})
	public static OptionalDouble ofDouble(@Nullable Double value) {
		return value == null ? OptionalDouble.empty() : OptionalDouble.of(value);
	}

	@SuppressWarnings({"ConditionalCanBeOptional", "AutoUnboxing"})
	public static OptionalLong ofLong(@Nullable Long value) {
		return value == null ? OptionalLong.empty() : OptionalLong.of(value);
	}

	@SuppressWarnings("AutoBoxing")
	public static @Nullable Integer valueOf(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") OptionalInt value) {
		return value.isPresent() ? value.getAsInt() : null;
	}

	@SuppressWarnings("AutoBoxing")
	public static @Nullable Double valueOf(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") OptionalDouble value) {
		return value.isPresent() ? value.getAsDouble() : null;
	}

	@SuppressWarnings("AutoBoxing")
	public static @Nullable Long valueOf(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") OptionalLong value) {
		return value.isPresent() ? value.getAsLong() : null;
	}
}
