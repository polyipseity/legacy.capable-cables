package $group__.$modId__.utilities.helpers;

import $group__.$modId__.utilities.constructs.interfaces.basic.IOperable;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static $group__.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;
import static $group__.$modId__.utilities.helpers.Miscellaneous.Comparables.greaterThan;
import static $group__.$modId__.utilities.helpers.Miscellaneous.Comparables.lessThan;
import static $group__.$modId__.utilities.helpers.Throwables.rejectArguments;
import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;

public enum Primitives {
	/* MARK empty */ ;


	/* SECTION static classes */

	public enum Numbers {
		/* MARK empty */ ;


		/* SECTION static methods */

		public static <N, NO extends IOperable<NO, N>> N negate(N n) {
			if (n instanceof IOperable<?, ?>) return castUnchecked(castUnchecked(n, (NO) null).negate());
			else if (n instanceof Number) {
				try { return castUnchecked(n.getClass().getDeclaredMethod("valueOf", String.class).invoke(null, ("-" + n.toString()).replace("--", ""))); } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) { throw rejectArguments(e, n); }
			}
			else throw rejectArguments(n);
		}

		@Nullable
		public static <N> N negateNullable(@Nullable N n) { return n == null ? null : negate(n); }


		public static <N, NO extends IOperable<NO, N>> N sum(N n, Collection<? extends N> e) {
			if (n instanceof IOperable<?, ?>) return castUnchecked(castUnchecked(n, (NO) null).sum(e));
			else if (n instanceof Number) {
				Class<?> clazz = n.getClass(), pClass;
				Method m, em;
				try {
					pClass = castUnchecked(clazz.getDeclaredField("TYPE").get(null));
					m = clazz.getDeclaredMethod("sum", pClass, pClass);
					em = Number.class.getDeclaredMethod(pClass + "Value"); clazz.getDeclaredMethod("valueOf", String.class);
					for (N t : e) n = castUnchecked(m.invoke(null, n, em.invoke(t)));
				} catch (NoSuchFieldException | SecurityException | IllegalAccessException | NoSuchMethodException | IllegalArgumentException | InvocationTargetException ex) { throw rejectArguments(ex, n, e); }
				return n;
			} else throw rejectArguments(n, e);
		}

		@Nullable
		public static <N> N sumNullable(Collection<? extends N> e) {
			switch (e.size()) {
				case 0:
					return null;
				case 1:
					return castUnchecked(e.toArray()[0]);
				default:
					List<? extends N> el = new ArrayList<>(e);
					return sum(el.get(0), el.subList(1, el.size()));
			}
		}

		@SuppressWarnings("varargs")
		@SafeVarargs
		@Nullable
		public static <N> N sumNullable(N... e) { return sumNullable(asList(e)); }

		public static <N> N sum(Collection<? extends N> e) { return requireNonNull(sumNullable(e)); }

		@SuppressWarnings("varargs")
		@SafeVarargs
		public static <N> N sum(N... e) { return requireNonNull(sumNullable(e)); }


		public static <N, NO extends IOperable<NO, N>> N max(N n, Collection<? extends N> e) {
			if (n instanceof IOperable<?, ?>) return castUnchecked(castUnchecked(n, (NO) null).min(e));
			else if (n instanceof Comparable<?>) {
				if (n instanceof Number) {
					Class<?> clazz = n.getClass(), pClass;
					Method m, em;
					try {
						pClass = castUnchecked(clazz.getDeclaredField("TYPE").get(null));
						m = clazz.getDeclaredMethod("sum", pClass, pClass);
						em = Number.class.getDeclaredMethod(pClass + "Value"); clazz.getDeclaredMethod("valueOf", String.class);
						for (N t : e) n = castUnchecked(m.invoke(null, n, em.invoke(t)));
					} catch (NoSuchFieldException | SecurityException | IllegalAccessException | NoSuchMethodException | IllegalArgumentException | InvocationTargetException ignored) {}
				}
				Comparable<N> r = castUnchecked(n);
				for (N t : e) if (lessThan(r, t)) r = castUnchecked(t);
				return castUnchecked(r);
			} else throw rejectArguments(n, e);
		}

		@Nullable
		public static <N> N maxNullable(Collection<? extends N> e) {
			switch (e.size()) {
				case 0:
					return null;
				case 1:
					return castUnchecked(e.toArray()[0]);
				default:
					List<? extends N> el = new ArrayList<>(e);
					return max(el.get(0), el.subList(1, el.size()));
			}
		}

		@SuppressWarnings("varargs")
		@SafeVarargs
		@Nullable
		public static <N> N maxNullable(N... e) { return maxNullable(asList(e)); }

		public static <N> N max(Collection<? extends N> e) { return requireNonNull(maxNullable(e)); }

		@SuppressWarnings("varargs")
		@SafeVarargs
		public static <N> N max(N... e) { return requireNonNull(maxNullable(e)); }


		public static <N, NO extends IOperable<NO, N>> N min(N n, Collection<? extends N> e) {
			if (n instanceof IOperable<?, ?>) return castUnchecked(castUnchecked(n, (NO) null).min(e));
			else if (n instanceof Comparable<?>) {
				if (n instanceof Number) {
					Class<?> clazz = n.getClass(), pClass;
					Method m, em;
					try {
						pClass = castUnchecked(clazz.getDeclaredField("TYPE").get(null));
						m = clazz.getDeclaredMethod("sum", pClass, pClass);
						em = Number.class.getDeclaredMethod(pClass + "Value"); clazz.getDeclaredMethod("valueOf", String.class);
						for (N t : e) n = castUnchecked(m.invoke(null, n, em.invoke(t)));
					} catch (NoSuchFieldException | SecurityException | IllegalAccessException | NoSuchMethodException | IllegalArgumentException | InvocationTargetException ignored) {}
				}
				Comparable<N> r = castUnchecked(n);
				for (N t : e) if (greaterThan(r, t)) r = castUnchecked(t);
				return castUnchecked(r);
			} else throw rejectArguments(n, e);
		}

		@Nullable
		public static <N> N minNullable(Collection<? extends N> e) {
			switch (e.size()) {
				case 0:
					return null;
				case 1:
					return castUnchecked(e.toArray()[0]);
				default:
					List<? extends N> el = new ArrayList<>(e);
					return min(el.get(0), el.subList(1, el.size()));
			}
		}

		@SuppressWarnings("varargs")
		@SafeVarargs
		@Nullable
		public static <N> N minNullable(N... e) { return minNullable(asList(e)); }

		public static <N> N min(Collection<? extends N> e) { return requireNonNull(minNullable(e)); }

		@SuppressWarnings("varargs")
		@SafeVarargs
		public static <N> N min(N... e) { return requireNonNull(minNullable(e)); }
	}
}
