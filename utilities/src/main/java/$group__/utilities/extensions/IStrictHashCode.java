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
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static $group__.utilities.helpers.Capacities.INITIAL_CAPACITY_2;
import static $group__.utilities.helpers.Capacities.INITIAL_CAPACITY_3;
import static $group__.utilities.helpers.Concurrency.MULTI_THREAD_THREAD_COUNT;
import static $group__.utilities.helpers.Dynamics.*;

public interface IStrictHashCode {
	/* SECTION static variables */

	Logger LOGGER = LogManager.getLogger(IStrictHashCode.class);

	LoadingCache<Class<?>, BiFunction<Object, Supplier<? extends Integer>, Integer>> FUNCTION_MAP =
			CacheBuilder.newBuilder().initialCapacity(INITIAL_CAPACITY_3).expireAfterAccess(MapsExtension.CACHE_EXPIRATION_ACCESS_DURATION, MapsExtension.CACHE_EXPIRATION_ACCESS_TIME_UNIT).concurrencyLevel(MULTI_THREAD_THREAD_COUNT).build(CacheLoader.from(k -> {
				assert k != null;

				ArrayList<BiFunction<Object, Supplier<? extends Integer>, Object>> ffs =
						new ArrayList<>(INITIAL_CAPACITY_2);
				if (k.getSuperclass() != Object.class) ffs.add((t, hcs) -> hcs.get());

				getThisAndSuperclasses(k).forEach(c -> {
					for (Field f : c.getDeclaredFields()) {
						if (isMemberStatic(f) || !isMemberFinal(f)) continue;
						@Nullable MethodHandle fm = Optionals.unboxOptional(Throwables.tryCall(() -> IMPL_LOOKUP.unreflectGetter(f),
								LOGGER));
						Throwables.consumeIfCaughtThrowable(t -> LOGGER.warn(() -> Loggers.EnumMessages.SUFFIX_WITH_THROWABLE.makeMessage(Loggers.EnumMessages.INVOCATION_UNABLE_TO_UNREFLECT_MEMBER.makeMessage("field getter", f, IMPL_LOOKUP), t)));
						if (fm == null) continue;

						ffs.add((t, hcs) -> Optionals.unboxOptional(Throwables.tryCallWithLogging(() -> fm.invoke(t), LOGGER)));
					}
				});

				return (t, hcs) -> ffs.stream().map(ff -> ff.apply(t, hcs)).collect(Collectors.toList()).hashCode();
			}));


	/* SECTION static methods */

	static int getHashCode(final Object thisObj, Supplier<? extends Integer> superHashCode) { return FUNCTION_MAP.getUnchecked(thisObj.getClass()).apply(thisObj, superHashCode); }


	/* SECTION methods */

	@Override
	@OverridingStatus(group = Constants.PACKAGE, when = When.ALWAYS)
	int hashCode();
}
