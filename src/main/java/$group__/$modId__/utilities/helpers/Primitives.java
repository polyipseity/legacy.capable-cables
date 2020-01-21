package $group__.$modId__.utilities.helpers;

import $group__.$modId__.utilities.constructs.interfaces.basic.IOperable;
import com.google.common.collect.Streams;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import static $group__.$modId__.utilities.helpers.Casts.castUnchecked;
import static $group__.$modId__.utilities.helpers.Comparables.greaterThan;
import static $group__.$modId__.utilities.helpers.Comparables.lessThan;
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


		public static <N, NO extends IOperable<NO, N>> N sum(N n, Iterable<? extends N> it) {
			if (n instanceof IOperable<?, ?>) return castUnchecked(castUnchecked(n, (NO) null).sum(it));
			else if (n instanceof Number) {
				Class<?> clazz = n.getClass(), pClass;
				Method m, em;
				try {
					pClass = castUnchecked(clazz.getDeclaredField("TYPE").get(null));
					m = clazz.getDeclaredMethod("sum", pClass, pClass);
					em = Number.class.getDeclaredMethod(pClass + "Value"); clazz.getDeclaredMethod("valueOf", String.class);
					for (N t : it) n = castUnchecked(m.invoke(null, n, em.invoke(t)));
				} catch (NoSuchFieldException | SecurityException | IllegalAccessException | NoSuchMethodException | IllegalArgumentException | InvocationTargetException ex) { throw rejectArguments(ex, n, it); }
				return n;
			} else throw rejectArguments(n, it);
		}

		@Nullable
		public static <N> N sumNullable(Iterable<? extends N> it) {
			@SuppressWarnings("UnstableApiUsage") List<? extends N> l = Streams.stream(it).collect(Collectors.toList());
			switch (l.size()) {
				case 0:
					return null;
				case 1:
					return l.get(0);
				default:
					return sum(l.get(0), l.subList(1, l.size()));
			}
		}

		@SuppressWarnings("varargs")
		@SafeVarargs
		@Nullable
		public static <N> N sumNullable(N... a) { return sumNullable(asList(a)); }

		public static <N> N sum(Iterable<? extends N> it) { return requireNonNull(sumNullable(it)); }

		@SuppressWarnings("varargs")
		@SafeVarargs
		public static <N> N sum(N... a) { return requireNonNull(sumNullable(a)); }


		public static <N, NO extends IOperable<NO, N>> N max(N n, Iterable<? extends N> it) {
			if (n instanceof IOperable<?, ?>) return castUnchecked(castUnchecked(n, (NO) null).min(it));
			else if (n instanceof Comparable<?>) {
				if (n instanceof Number) {
					Class<?> clazz = n.getClass(), pClass;
					Method m, em;
					try {
						pClass = castUnchecked(clazz.getDeclaredField("TYPE").get(null));
						m = clazz.getDeclaredMethod("sum", pClass, pClass);
						em = Number.class.getDeclaredMethod(pClass + "Value"); clazz.getDeclaredMethod("valueOf", String.class);
						for (N t : it) n = castUnchecked(m.invoke(null, n, em.invoke(t)));
					} catch (NoSuchFieldException | SecurityException | IllegalAccessException | NoSuchMethodException | IllegalArgumentException | InvocationTargetException ignored) { /* MARK empty */ }
				}
				Comparable<N> r = castUnchecked(n);
				for (N t : it) if (lessThan(r, t)) r = castUnchecked(t);
				return castUnchecked(r);
			} else throw rejectArguments(n, it);
		}

		@Nullable
		public static <N> N maxNullable(Iterable<? extends N> it) {
			@SuppressWarnings("UnstableApiUsage") List<? extends N> l = Streams.stream(it).collect(Collectors.toList());
			switch (l.size()) {
				case 0:
					return null;
				case 1:
					return l.get(0);
				default:
					return max(l.get(0), l.subList(1, l.size()));
			}
		}

		@SuppressWarnings("varargs")
		@SafeVarargs
		@Nullable
		public static <N> N maxNullable(N... a) { return maxNullable(asList(a)); }

		public static <N> N max(Iterable<? extends N> it) { return requireNonNull(maxNullable(it)); }

		@SuppressWarnings("varargs")
		@SafeVarargs
		public static <N> N max(N... a) { return requireNonNull(maxNullable(a)); }


		public static <N, NO extends IOperable<NO, N>> N min(N n, Iterable<? extends N> it) {
			if (n instanceof IOperable<?, ?>) return castUnchecked(castUnchecked(n, (NO) null).min(it));
			else if (n instanceof Comparable<?>) {
				if (n instanceof Number) {
					Class<?> clazz = n.getClass(), pClass;
					Method m, em;
					try {
						pClass = castUnchecked(clazz.getDeclaredField("TYPE").get(null));
						m = clazz.getDeclaredMethod("sum", pClass, pClass);
						em = Number.class.getDeclaredMethod(pClass + "Value"); clazz.getDeclaredMethod("valueOf", String.class);
						for (N t : it) n = castUnchecked(m.invoke(null, n, em.invoke(t)));
					} catch (NoSuchFieldException | SecurityException | IllegalAccessException | NoSuchMethodException | IllegalArgumentException | InvocationTargetException ignored) { /* MARK empty */ }
				}
				Comparable<N> r = castUnchecked(n);
				for (N t : it) if (greaterThan(r, t)) r = castUnchecked(t);
				return castUnchecked(r);
			} else throw rejectArguments(n, it);
		}

		@Nullable
		public static <N> N minNullable(Iterable<? extends N> it) {
			@SuppressWarnings("UnstableApiUsage") List<? extends N> l = Streams.stream(it).collect(Collectors.toList());
			switch (l.size()) {
				case 0:
					return null;
				case 1:
					return l.get(0);
				default:
					return min(l.get(0), l.subList(1, l.size()));
			}
		}

		@SuppressWarnings("varargs")
		@SafeVarargs
		@Nullable
		public static <N> N minNullable(N... a) { return minNullable(asList(a)); }

		public static <N> N min(Iterable<? extends N> it) { return requireNonNull(minNullable(it)); }

		@SuppressWarnings("varargs")
		@SafeVarargs
		public static <N> N min(N... a) { return requireNonNull(minNullable(a)); }
	}
}
