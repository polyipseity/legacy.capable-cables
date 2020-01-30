package $group__.$modId__.utilities.helpers;

import $group__.$modId__.utilities.variables.Constants;

import javax.annotation.Nullable;

import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxed;
import static $group__.$modId__.utilities.helpers.Throwables.rejectArguments;

public enum Miscellaneous {
	/* MARK empty */;


	/* SECTION static methods */

	public static <T> T markUnused(Class<T> t) {
		return Constants.PRIMITIVE_DATA_TYPE_MAP.entrySet().stream().filter(e -> e.getKey().isAssignableFrom(t)).findFirst().<T>map(e -> castUncheckedUnboxed(e.getValue())).orElseThrow(() -> rejectArguments(t));
	}

	@SuppressWarnings("SameReturnValue")
	@Nullable
	public static <T> T markUnused() { return null; }


	public static Object newLockObject() { return new Object(); }


	/**
	 * Function K of SKI combinator calculus.
	 *
	 * @param x   constant
	 * @param y   any
	 * @param <T> type of parameter <pre>{@code x}</pre>
	 *
	 * @return parameter <pre>{@code x}</pre>
	 */
	public static <T> T K(T x, @SuppressWarnings("unused") Object... y) { return x; }
}
