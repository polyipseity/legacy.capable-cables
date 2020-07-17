package $group__.utilities.helpers;

import javax.annotation.Nullable;

import static $group__.utilities.helpers.Casts.castUncheckedUnboxed;
import static $group__.utilities.helpers.Primitives.PRIMITIVE_DATA_TYPE_TO_DEFAULT_VALUE_MAP;
import static java.util.Objects.requireNonNull;

/**
 * Contains utilities that are hard or too small to be categorized.
 * See each method's description for more details.
 *
 * @author William So
 * @since 0.0.1.0
 */
public enum MiscellaneousUtilities {
	/* MARK empty */;


	@Nullable
	public static <T> T getDefaultValue(@Nullable Class<T> type) { return type == null ? null : PRIMITIVE_DATA_TYPE_TO_DEFAULT_VALUE_MAP.entrySet().stream().filter(e -> e.getKey().isAssignableFrom(type)).findFirst().<T>map(e -> castUncheckedUnboxed(e.getValue())).orElse(null); }

	public static <T> T getDefaultValueNonnull(Class<T> type) { return requireNonNull(getDefaultValue(type)); }


	/**
	 * Marks something as a {@code synchronized} lock.
	 *
	 * @return a new {@code Object}
	 *
	 * @since 0.0.1.0
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
	 * @since 0.0.1.0
	 */
	public static <T> T K(T x, @SuppressWarnings("unused") Object... y) { return x; }
}
