package $group__.$modId__.utilities.helpers;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Function;

import static $group__.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;

public enum Miscellaneous {
	/* MARK empty */ ;


	/* SECTION static classes */

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
		public static <O, T extends O> T castChecked(O o, @Nullable T t, Function<O, T> callback) { try { return castUnchecked(o, t); } catch (ClassCastException e) { return callback.apply(o); } }

		@Nullable
		public static <O, T extends O> T castChecked(O o, @Nullable T t) { return castChecked(o, t, t1 -> null); }

		@Nullable
		public static <O, T extends O> T castChecked(O o, Function<O, T> callback) { return castChecked(o, null, callback); }

		@Nullable
		public static <O, T extends O> T castChecked(O o) { return castChecked(o, t -> null); }
	}


	@SuppressWarnings("SpellCheckingInspection")
	public enum Comparables {
		/* MARK empty */ ;


		/* SECTION static methods */

		public static <T> boolean lessThan(Comparable<T> a, T b) { return a.compareTo(b) < 0; }

		public static <T> boolean equalTo(Comparable<T> a, T b) { return a.compareTo(b) == 0; }

		public static <T> boolean greaterThan(Comparable<T> a, T b) { return a.compareTo(b) > 0; }

		public static <T> boolean lessThanOrEqualTo(Comparable<T> a, T b) { return a.compareTo(b) <= 0; }

		public static <T> boolean greaterThanOrEqualTo(Comparable<T> a, T b) { return a.compareTo(b) >= 0; }
	}


	public enum Reflections {
		/* MARK empty */ ;


		/* SECTION static methods */

		public static boolean isClassAbstract(Class<?> c) { return c.isInterface() || Modifier.isAbstract(c.getModifiers()); }


		public static String getMethodNameDescriptor(Method m) { return m.getName() + org.objectweb.asm.Type.getMethodDescriptor(m); }


		public static boolean isFormerMethodOverriddenByLatter(Method a, Method b) {
			if (!getMethodNameDescriptor(a).equals(getMethodNameDescriptor(b))) return false;
			Class<?>[] ap = a.getParameterTypes();
			Class<?>[] bp = a.getParameterTypes();
			if (ap.length != bp.length) return false;
			for (int i = 0; i < ap.length; i++) { if (!ap[i].isAssignableFrom(bp[i])) return false; }
			return true;
		}


		public static Type[] getGenericSuperclassActualTypeArguments(Class<?> c) { return ((ParameterizedType) c.getGenericSuperclass()).getActualTypeArguments(); }

		public static <T extends Type> T getGenericSuperclassActualTypeArgument(Class<?> c, int i) { return castUnchecked(getGenericSuperclassActualTypeArguments(c)[i]); }
	}
}
