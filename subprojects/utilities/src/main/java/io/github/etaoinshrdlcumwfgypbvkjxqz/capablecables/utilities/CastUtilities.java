package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;

import java.util.Optional;

public enum CastUtilities {
	;

	public static <T> T castUnchecked(Object obj) { return AssertionUtilities.assertNonnull(castUncheckedNullable(obj)); }

	public static <T> T upcast(T obj) { return obj; }

	@SuppressWarnings("unchecked")
	@Nullable
	public static <T> T castUncheckedNullable(@Nullable Object obj) { return (T) obj; }

	public static <T> Optional<T> castChecked(Class<? extends T> clazz, @Nullable Object obj) {
		if (clazz.isInstance(obj))
			return Optional.of(clazz.cast(obj));
		return Optional.empty();
	}
}
