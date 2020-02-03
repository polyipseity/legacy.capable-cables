package $group__.$modId__.utilities.constructs.interfaces.basic;

import javax.annotation.Nullable;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;

import static $group__.$modId__.utilities.helpers.MapsExtension.MULTI_THREAD_MAP_MAKER;
import static $group__.$modId__.utilities.helpers.Optionals.unboxOptional;
import static $group__.$modId__.utilities.helpers.Reflections.*;
import static $group__.$modId__.utilities.helpers.Throwables.tryCallWithLogging;
import static $group__.$modId__.utilities.variables.Globals.LOGGER;

public interface IDirty {
	/* SECTION static variables */

	ConcurrentMap<Class<?>, BiFunction<IDirty, Boolean, Object>> FUNCTION_MAP = MULTI_THREAD_MAP_MAKER.makeMap();


	/* SECTION static methods */

	static Object tryDirtiness(IDirty thisObj, boolean mark) {
		Class<? extends IDirty> tc = thisObj.getClass();
		@Nullable BiFunction<IDirty, Boolean, Object> func = FUNCTION_MAP.get(tc);
		if (func == null) {
			final int[] fs = {0};

			ArrayList<BiFunction<IDirty, Boolean, Object>> dfs = new ArrayList<>();
			getThisAndSuperclasses(tc).forEach(c -> {
				for (Field f : c.getDeclaredFields()) {
					if (isMemberStatic(f)) continue;
					@Nullable MethodHandle fmg = unboxOptional(tryCallWithLogging(() -> IMPL_LOOKUP.unreflectGetter(f), LOGGER));
					if (fmg == null) continue;

					@Nullable BiFunction<IDirty, Boolean, Object> df;
					Class<?> ft = f.getType();
					if ("dirtiness".equals(f.getName()) && long.class.isAssignableFrom(ft)) {
						fs[0]++;
						@Nullable MethodHandle fms = unboxOptional(tryCallWithLogging(() -> IMPL_LOOKUP.unreflectSetter(f), LOGGER));
						if (fms == null) continue;
						df = (t, z) -> unboxOptional(tryCallWithLogging(z ? (() -> fms.invoke(t, fmg.invoke(t))) : (() -> fmg.invoke(t)), LOGGER));
					} else if (IDirty.class.isAssignableFrom(ft)) df = (t, z) -> z ? null : unboxOptional(tryCallWithLogging(() -> ((IDirty) fmg.invoke(t)).getDirtiness(), LOGGER));
					else continue;

					dfs.add(df);
				}
			});
			FUNCTION_MAP.put(tc, func = (t, z) -> z ? dfs.stream().map(df -> df.apply(t, true)) : dfs.stream().map(df -> (Long) df.apply(t, false)).filter(Objects::nonNull).reduce(0L, Long::sum));

			switch (fs[0]) {
				case 0:
					LOGGER.warn("No 'dirty' boolean fields for object '{}' of class '{}'", thisObj, tc);
					break;
				case 1:
					break;
				default:
					LOGGER.warn("Too many ({}) 'dirty' boolean fields for object '{}' of class '{}'", fs[0], thisObj, tc);
			}
		}
		return func.apply(thisObj, mark);
	}

	static long tryGetDirtiness(IDirty thisObj) { return (long) tryDirtiness(thisObj, false); }

	static void tryMarkDirty(IDirty thisObj) { tryDirtiness(thisObj, true); }

	static boolean isDirty(IDirty dirty, AtomicLong dirtiness) {
		long t = dirty.getDirtiness(), o = dirtiness.getAndSet(t);
		return t > o || Long.signum(t) != Long.signum(o);
	}


	/* SECTION methods */

	default void markDirty() { tryMarkDirty(this); }

	default long getDirtiness() { return tryGetDirtiness(this); }
}
