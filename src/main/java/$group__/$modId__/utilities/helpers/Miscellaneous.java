package $group__.$modId__.utilities.helpers;

import $group__.$modId__.utilities.variables.Constants;

import javax.annotation.Nullable;

import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxed;
import static $group__.$modId__.utilities.helpers.specific.Throwables.rejectArguments;

/**
 * Contains utilities that are hard or too small to be categorized.
 * See each method's description for more details.
 *
 * @author William So
 * @since 0.0.1.0
 */
public enum Miscellaneous {
	/* MARK empty */;


	/* SECTION static methods */

	/**
	 * Marks something as unused.
	 *
	 * @param t a primitive data type
	 * @param <T> type of parameter {@code t}
	 * @return default value for parameter type {@code <T>}
	 * @throws IllegalArgumentException when parameter type {@code <T>} is not a primitive data type
	 * @see Constants#PRIMITIVE_DATA_TYPE_ARRAY primitive data type array
	 * @since 0.0.1.0
	 */
	public static <T> T markUnused(Class<T> t) {
		return Constants.PRIMITIVE_DATA_TYPE_TO_DEFAULT_VALUE_MAP.entrySet().stream().filter(e -> e.getKey().isAssignableFrom(t)).findFirst().<T>map(e -> castUncheckedUnboxed(e.getValue())).orElseThrow(() -> rejectArguments(t));
	}

	/**
	 * Marks something as unused.
	 *
	 * @param <T> a non-primitive data type
	 * @return {@code null}
	 * @throws NullPointerException when parameter type {@code <T>} is a primitive data type
	 * @since 0.0.1.0
	 */
	@SuppressWarnings("SameReturnValue")
	@Nullable
	public static <T> T markUnused() { return null; }


	/**
	 * Marks something as a {@code synchronized} lock.
	 *
	 * @return a new {@code Object}
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
	 * @return parameter {@code x}
	 * @since 0.0.1.0
	 */
	public static <T> T K(T x, @SuppressWarnings("unused") Object... y) { return x; }
}
