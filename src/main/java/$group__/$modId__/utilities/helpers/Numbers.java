package $group__.$modId__.utilities.helpers;

import $group__.$modId__.utilities.constructs.interfaces.basic.IOperable;
import com.google.common.collect.Lists;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;

import static $group__.$modId__.utilities.helpers.Casts.*;
import static $group__.$modId__.utilities.helpers.Comparables.greaterThan;
import static $group__.$modId__.utilities.helpers.Comparables.lessThan;
import static $group__.$modId__.utilities.helpers.Optionals.unboxOptional;
import static $group__.$modId__.utilities.helpers.Patterns.TWO_MINUS_SIGNS_PATTERN;
import static $group__.$modId__.utilities.helpers.Reflections.Classes.getDeclaredField;
import static $group__.$modId__.utilities.helpers.Reflections.Classes.getDeclaredMethod;
import static java.lang.Double.doubleToLongBits;
import static java.util.Arrays.asList;

public enum Numbers {
	/* MARK empty */;


	/* SECTION static methods */

	public static boolean isNegative(Number value) {
		double vd = value.doubleValue();
		return (doubleToLongBits(vd) & 0x8000000000000000L) != 0L && !Double.isNaN(vd);
	}


	public static <N> Optional<N> negate(@Nullable N n) {
		if (n == null)
			return Optional.empty();
		else if (n instanceof IOperable<?, ?>)
			return Casts.<IOperable<?, N>>castChecked(n, castUncheckedUnboxedNonnull(IOperable.class)).flatMap(t -> castChecked(t.negate(), castUncheckedUnboxedNonnull(t.getClass())));
		else if (n instanceof Number)
			return getDeclaredMethod(n.getClass(), "valueOf", String.class).invoke(null, TWO_MINUS_SIGNS_PATTERN.matcher(("-" + n)).replaceAll(Matcher.quoteReplacement(StringsExtension.EMPTY))).flatMap(t -> castChecked(t, castUncheckedUnboxedNonnull(t.getClass())));
		return Optional.empty();
	}

	@SuppressWarnings("varargs")
	@SafeVarargs
	public static <N> Optional<N> sum(N... a) { return sum(asList(a)); }

	public static <N> Optional<N> sum(Iterable<? extends N> it) {
		List<? extends N> l = Lists.newArrayList(it);
		switch (l.size()) {
			case 0:
				return Optional.empty();
			case 1:
				return Optional.ofNullable(l.get(0));
			default:
				return sum(l.get(0), l.subList(1, l.size()));
		}
	}

	public static <N> Optional<N> sum(@Nullable N n, Iterable<? extends N> it) {
		if (n instanceof IOperable<?, ?>)
			return Casts.<IOperable<?, N>>castChecked(n, castUncheckedUnboxedNonnull(IOperable.class)).flatMap(t -> castChecked(t.sum(it), castUncheckedUnboxedNonnull(t.getClass())));
		else if (n instanceof Number) {
			Class<? extends N> clazz = castUncheckedUnboxedNonnull(n.getClass());
			@Nullable Class<?> pClass = unboxOptional(getDeclaredField(clazz, "TYPE").get(null).flatMap(t -> castChecked(t, Class.class)));
			if (pClass == null) return Optional.empty();

			Reflections.Classes.AccessibleObjectAdapter.MethodAdapter m = getDeclaredMethod(clazz, "sum", pClass, pClass),
					em = getDeclaredMethod(Number.class, pClass + "Value");

			Iterator<? extends N> itr = it.iterator();
			while (itr.hasNext() && n != null)
				n = unboxOptional(m.invoke(null, n, unboxOptional(em.invoke(itr.next()))).flatMap(t -> castChecked(t, clazz)));
			return Optional.ofNullable(n);
		}
		return Optional.empty();
	}

	@SuppressWarnings("varargs")
	@SafeVarargs
	public static <N> Optional<N> max(N... a) { return max(asList(a)); }

	public static <N> Optional<N> max(Iterable<? extends N> it) {
		List<? extends N> l = Lists.newArrayList(it);
		switch (l.size()) {
			case 0:
				return Optional.empty();
			case 1:
				return Optional.ofNullable(l.get(0));
			default:
				return max(l.get(0), l.subList(1, l.size()));
		}
	}

	public static <N> Optional<N> max(@Nullable N n, Iterable<? extends N> it) {
		if (n instanceof IOperable<?, ?>)
			return Casts.<IOperable<?, N>>castChecked(n, castUncheckedUnboxedNonnull(IOperable.class)).flatMap(t -> castChecked(t.max(it), castUncheckedUnboxedNonnull(t.getClass())));
		else if (n instanceof Comparable<?>) {
			@Nullable Comparable<N> t = castCheckedUnboxed(n, castUncheckedUnboxedNonnull(Comparable.class));
			Iterator<? extends N> itr = it.iterator();
			while (itr.hasNext() && t != null) {
				N t1 = itr.next();
				if (lessThan(t, t1)) t = castCheckedUnboxed(t1, castUncheckedUnboxedNonnull(Comparable.class));
			}
			return castChecked(t, castUncheckedUnboxedNonnull(n.getClass()));
		}
		return Optional.empty();
	}

	@SuppressWarnings("varargs")
	@SafeVarargs
	public static <N> Optional<N> min(N... a) { return min(asList(a)); }

	public static <N> Optional<N> min(Iterable<? extends N> it) {
		List<? extends N> l = Lists.newArrayList(it);
		switch (l.size()) {
			case 0:
				return Optional.empty();
			case 1:
				return Optional.ofNullable(l.get(0));
			default:
				return min(l.get(0), l.subList(1, l.size()));
		}
	}

	public static <N> Optional<N> min(@Nullable N n, Iterable<? extends N> it) {
		if (n instanceof IOperable<?, ?>)
			return Casts.<IOperable<?, N>>castChecked(n, castUncheckedUnboxedNonnull(IOperable.class)).flatMap(t -> castChecked(t.min(it), castUncheckedUnboxedNonnull(t.getClass())));
		else if (n instanceof Comparable<?>) {
			@Nullable Comparable<N> t = castCheckedUnboxed(n, castUncheckedUnboxedNonnull(Comparable.class));
			Iterator<? extends N> itr = it.iterator();
			while (itr.hasNext() && t != null) {
				N t1 = itr.next();
				if (greaterThan(t, t1)) t = castCheckedUnboxed(t1, castUncheckedUnboxedNonnull(Comparable.class));
			}
			return castChecked(t, castUncheckedUnboxedNonnull(n.getClass()));
		}
		return Optional.empty();
	}
}
