package $group__.utilities;

import javax.annotation.Nullable;
import java.util.Optional;

import static $group__.utilities.Casts.castUncheckedUnboxed;
import static $group__.utilities.Primitives.PRIMITIVE_DATA_TYPE_TO_DEFAULT_VALUE_MAP;

/**
 * Contains utilities that are hard or too small to be categorized.
 * See each method's description for more details.
 *
 * @author William So
 * @since 0.0.1
 */
public enum MiscellaneousUtilities {
	/* MARK empty */;

	public static <T> Optional<T> getDefaultValue(@Nullable Class<T> type) { return Optional.ofNullable(type).flatMap(tClass -> PRIMITIVE_DATA_TYPE_TO_DEFAULT_VALUE_MAP.entrySet().stream().filter(e -> e.getKey().isAssignableFrom(tClass)).findFirst().map(e -> castUncheckedUnboxed(e.getValue()))); }


	/**
	 * Marks something as a {@code synchronized} lock.
	 *
	 * @return a new {@code Object}
	 *
	 * @since 0.0.1
	 */
	public static Object newLockObject() { return new Object(); }


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
	public static <T> T K(T x, @SuppressWarnings("unused") Object... y) { return x; }

	@Nullable
	public static <T> T KNullable(@Nullable T x, @SuppressWarnings("unused") Object... y) { return x; }
}
