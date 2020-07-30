package $group__.utilities;

import $group__.utilities.specific.StreamUtilities;

import javax.annotation.Nullable;
import java.util.Optional;

import static $group__.utilities.CastUtilities.castUnchecked;
import static $group__.utilities.PrimitiveUtilities.PRIMITIVE_DATA_TYPE_TO_DEFAULT_VALUE_MAP;

/**
 * Contains utilities that are hard or too small to be categorized.
 * See each method's description for more details.
 *
 * @author William So
 * @since 0.0.1
 */
public enum MiscellaneousUtilities {
	/* MARK empty */;

	public static <T> Optional<T> getDefaultValue(@Nullable Class<T> type) {
		return Optional.ofNullable(type)
				.flatMap(tClass -> StreamUtilities.streamSmart(PRIMITIVE_DATA_TYPE_TO_DEFAULT_VALUE_MAP.entrySet(), 3).filter(e -> e.getKey().isAssignableFrom(tClass)).findFirst())
				.map(e -> castUnchecked(e.getValue()));
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
	 * @since 0.0.1
	 */
	public static <T> T k(T x, @SuppressWarnings("unused") Object... y) { return x; }

	@Nullable
	public static <T> T kNullable(@Nullable T x, @SuppressWarnings("unused") Object... y) { return x; }
}
