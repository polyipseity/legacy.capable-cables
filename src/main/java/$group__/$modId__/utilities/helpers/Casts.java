package $group__.$modId__.utilities.helpers;

import javax.annotation.Nullable;
import java.util.function.Function;

public enum Casts {
	/* MARK empty */ ;


	/* SECTION static methods */

	@SuppressWarnings("unchecked")
	public static <T, R extends T> R castUncheckedExtended(Object o, @SuppressWarnings("unused") @Nullable T t) throws ClassCastException { return (R) o; }

	public static <T, R extends T> R castUncheckedExtended(T o) throws ClassCastException { return castUncheckedExtended(o, null); }

	public static <T> T castUnchecked(Object o, @Nullable T t) { return castUncheckedExtended(o, t); }

	public static <T> T castUnchecked(Object o) { return castUncheckedExtended(o); }

	@SuppressWarnings("ConstantConditions")
	@Nullable
	public static <T> T castUncheckedExtendedNullable(@Nullable Object o, @Nullable T t) { return castUncheckedExtended(o, t); }

	@Nullable
	public static <T> T castUncheckedExtendedNullable(@Nullable Object o) { return castUncheckedExtendedNullable(o, null); }

	@SuppressWarnings("ConstantConditions")
	@Nullable
	public static <T> T castUncheckedNullable(@Nullable Object o, @Nullable T t) { return castUnchecked(o, t); }

	@Nullable
	public static <T> T castUncheckedNullable(@Nullable Object o) { return castUncheckedNullable(o, null); }


	@Nullable
	public static <O, T extends O> T castChecked(Function<O, T> callback, O o, @Nullable T t) { try { return castUnchecked(o, t); } catch (ClassCastException e) { return callback.apply(o); } }

	@Nullable
	public static <O, T extends O> T castChecked(O o, @Nullable T t) { return castChecked(t1 -> null, o, t); }

	@Nullable
	public static <O, T extends O> T castChecked(Function<O, T> callback, O o) { return castChecked(callback, o, null); }

	@Nullable
	public static <O, T extends O> T castChecked(O o) { return castChecked(t -> null, o); }
}
