package $group__.$modId__.traits.basic;

import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;

import static $group__.$modId__.utilities.helpers.Dynamics.*;
import static $group__.$modId__.utilities.helpers.specific.Loggers.EnumMessages.*;
import static $group__.$modId__.utilities.helpers.specific.MapsExtension.MAP_MAKER_MULTI_THREAD;
import static $group__.$modId__.utilities.helpers.specific.Optionals.unboxOptional;
import static $group__.$modId__.utilities.helpers.specific.Throwables.*;
import static $group__.$modId__.utilities.variables.Constants.INITIAL_SIZE_SMALL;

public interface IDirty {
	/* SECTION static variables */

	ConcurrentMap<Class<?>, BiFunction<IDirty, Boolean, Object>> FUNCTION_MAP = MAP_MAKER_MULTI_THREAD.makeMap();


	/* SECTION static methods */

	static Object tryDirtiness(IDirty thisObj, boolean mark, Logger logger) {
		Class<? extends IDirty> tc = thisObj.getClass();
		@Nullable BiFunction<IDirty, Boolean, Object> func = FUNCTION_MAP.get(tc);
		if (func == null) {
			final int[] fs = {0};

			ArrayList<BiFunction<IDirty, Boolean, Object>> dfs = new ArrayList<>(INITIAL_SIZE_SMALL);
			getThisAndSuperclasses(tc).forEach(c -> {
				for (Field f : c.getDeclaredFields()) {
					if (isMemberStatic(f)) continue;

					@Nullable MethodHandle fmg = unboxOptional(tryCall(() -> IMPL_LOOKUP.unreflectGetter(f), logger));
					consumeIfCaughtThrowable(t -> logger.warn(() -> SUFFIX_WITH_THROWABLE.makeMessage(INVOCATION_UNABLE_TO_UNREFLECT_MEMBER.makeMessage("field getter", f, IMPL_LOOKUP), t)));
					if (fmg == null) continue;

					@Nullable BiFunction<IDirty, Boolean, Object> df;
					Class<?> ft = f.getType();
					if ("dirtiness".equals(f.getName()) && long.class.isAssignableFrom(ft)) {
						fs[0]++;

						@Nullable MethodHandle fms = unboxOptional(tryCall(() -> IMPL_LOOKUP.unreflectSetter(f), logger));
						consumeIfCaughtThrowable(t -> logger.warn(() -> SUFFIX_WITH_THROWABLE.makeMessage(INVOCATION_UNABLE_TO_UNREFLECT_MEMBER.makeMessage("field setter", f, IMPL_LOOKUP), t)));
						if (fms == null) continue;

						df = (t, z) -> unboxOptional(tryCallWithLogging(z ? (() -> fms.invoke(t, fmg.invoke(t))) : (() -> fmg.invoke(t)), logger));
					} else if (IDirty.class.isAssignableFrom(ft)) df = (t, z) -> z ? null : unboxOptional(tryCallWithLogging(() -> ((IDirty) fmg.invoke(t)).getDirtiness(logger), logger));
					else continue;

					dfs.add(df);
				}
			});
			FUNCTION_MAP.put(tc, func = (t, z) -> z ? dfs.stream().map(df -> df.apply(t, true)) : dfs.stream().map(df -> (Long) df.apply(t, false)).filter(Objects::nonNull).reduce(0L, Long::sum));

			switch (fs[0]) {
				case 0:
					logger.warn(() -> FACTORY_PARAMETERIZED_MESSAGE.makeMessage("No 'dirtiness' long fields for object '{}' of class '{}'", thisObj, tc.toGenericString()));
					break;
				case 1:
					break;
				default:
					logger.warn(() -> FACTORY_PARAMETERIZED_MESSAGE.makeMessage("Too many ({}) 'dirtiness' long fields for object '{}' of class '{}'", fs[0], thisObj, tc.toGenericString()));
			}
		}
		return func.apply(thisObj, mark);
	}

	static long tryGetDirtiness(IDirty thisObj, Logger logger) { return (long) tryDirtiness(thisObj, false, logger); }

	static void tryMarkDirty(IDirty thisObj, Logger logger) { tryDirtiness(thisObj, true, logger); }

	static boolean isDirty(IDirty dirty, AtomicLong dirtiness, Logger logger) {
		long t = dirty.getDirtiness(logger), o = dirtiness.getAndSet(t);
		return t > o || Long.signum(t) != Long.signum(o);
	}


	/* SECTION methods */

	default void markDirty(Logger logger) { tryMarkDirty(this, logger); }

	default long getDirtiness(Logger logger) { return tryGetDirtiness(this, logger); }
}
