package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.primitives.PrimitiveUtilities.getPrimitiveDataTypeToDefaultValueMap;

/**
 * Contains utilities that are hard or too small to be categorized.
 * See each method's description for more details.
 *
 * @author William So
 * @since Capable Cables 0.0.1
 */
public enum MiscellaneousUtilities {
	;

	@SuppressWarnings("unchecked")
	public static <T> Optional<T> getDefaultValue(@Nullable Class<T> type) {
		return Optional.ofNullable(type)
				.map(t -> getPrimitiveDataTypeToDefaultValueMap().entrySet().stream().unordered()
						.filter(e -> AssertionUtilities.assertNonnull(e.getKey()).isAssignableFrom(t)))
				.flatMap(Stream::findAny)
				.map(Map.Entry::getValue)
				.map(e -> (T) e);
	}

	/**
	 * Returns parameter {@code x}.
	 * <p>
	 * This method is the function K of SKI combinator calculus.
	 *
	 * @param x   {@code return} value
	 * @param y   any
	 * @param <T> type of parameter {@code x}
	 *
	 * @return parameter {@code x}
	 *
	 * @since Capable Cables 0.0.1
	 */
	public static <T> T k(T x, @SuppressWarnings("unused") Object... y) { return x; }

	@Nullable
	public static <T> T kNullable(@Nullable T x, @SuppressWarnings("unused") Object... y) { return x; }
}
