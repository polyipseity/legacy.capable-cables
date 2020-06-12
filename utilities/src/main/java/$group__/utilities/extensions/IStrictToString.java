package $group__.utilities.extensions;

import $group__.annotations.OverridingStatus;
import $group__.utilities.Constants;
import $group__.utilities.helpers.specific.Loggers;
import $group__.utilities.helpers.specific.MapsExtension;
import $group__.utilities.helpers.specific.Optionals;
import $group__.utilities.helpers.specific.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.meta.When;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.Function;

import static $group__.utilities.helpers.Capacities.INITIAL_CAPACITY_2;
import static $group__.utilities.helpers.Capacities.INITIAL_CAPACITY_3;
import static $group__.utilities.helpers.Concurrency.MULTI_THREAD_THREAD_COUNT;
import static $group__.utilities.helpers.Dynamics.*;

public interface IStrictToString {
	/* SECTION static variables */

	Logger LOGGER = LogManager.getLogger(IStrictToString.class);

	LoadingCache<Class<?>, BiFunction<Object, String, String>> FUNCTION_MAP =
			CacheBuilder.newBuilder().initialCapacity(INITIAL_CAPACITY_3).expireAfterAccess(MapsExtension.CACHE_EXPIRATION_ACCESS_DURATION, MapsExtension.CACHE_EXPIRATION_ACCESS_TIME_UNIT).concurrencyLevel(MULTI_THREAD_THREAD_COUNT).build(CacheLoader.from(k -> {
				assert k != null;

				final boolean[] first = {true};
				ArrayList<BiFunction<Object, String, String>> sfs = new ArrayList<>(INITIAL_CAPACITY_2);
				getThisAndSuperclasses(k).forEach(c -> {
					for (Field f : c.getDeclaredFields()) {
						if (isMemberStatic(f)) continue;
						String fnp = f.getName() + '=';
						@Nullable MethodHandle fm = Optionals.unboxOptional(Throwables.tryCall(() -> IMPL_LOOKUP.unreflectGetter(f),
								LOGGER));
						Throwables.consumeIfCaughtThrowable(t -> LOGGER.warn(() -> Loggers.EnumMessages.SUFFIX_WITH_THROWABLE.makeMessage(Loggers.EnumMessages.INVOCATION_UNABLE_TO_UNREFLECT_MEMBER.makeMessage("field getter", f, IMPL_LOOKUP), t)));

						Function<Object, String> ff;
						if (fm == null) {
							@Nullable Throwable cau = Throwables.getCaughtThrowableStatic();
							ff = t -> "!!!Thrown{throwable=" + cau + "}!!!";
						} else
							ff = t -> fnp + Optionals.unboxOptional(Throwables.tryCallWithLogging(() -> fm.invoke(t), LOGGER));

						if (!first[0]) sfs.add((t, ss) -> ", ");
						sfs.add((t, ss) -> fnp + ff.apply(t));
						first[0] = false;
					}
				});
				if (!first[0]) sfs.add((t, ss) -> ", ");
				sfs.add((t, ss) -> "super=" + ss + '}');

				String tcp = k.getSimpleName() + '{';
				return (t, ss) -> sfs.stream().reduce(tcp, (t1, u) -> t1 + u.apply(t, ss), String::concat);
			}));


	/* SECTION static methods */

	static String getToStringString(final Object thisObj, String superString) { return FUNCTION_MAP.getUnchecked(thisObj.getClass()).apply(thisObj, superString); }


	/* SECTION methods */

	@Override
	@OverridingStatus(group = Constants.PACKAGE, when = When.ALWAYS)
	String toString();
}
