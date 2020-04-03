package $group__.$modId__.utilities.extensions;

import $group__.$modId__.annotations.OverridingStatus;
import $group__.$modId__.utilities.helpers.Dynamics;
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
import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import static $group__.$modId__.utilities.Constants.PACKAGE;
import static $group__.$modId__.utilities.helpers.Capacities.INITIAL_CAPACITY_2;
import static $group__.$modId__.utilities.helpers.Capacities.INITIAL_CAPACITY_3;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Concurrency.MULTI_THREAD_THREAD_COUNT;
import static $group__.$modId__.utilities.helpers.Dynamics.*;
import static $group__.$modId__.utilities.helpers.specific.Loggers.EnumMessages.INVOCATION_UNABLE_TO_UNREFLECT_MEMBER;
import static $group__.$modId__.utilities.helpers.specific.Loggers.EnumMessages.SUFFIX_WITH_THROWABLE;
import static $group__.$modId__.utilities.helpers.specific.MapsExtension.CACHE_EXPIRATION_ACCESS_DURATION;
import static $group__.$modId__.utilities.helpers.specific.MapsExtension.CACHE_EXPIRATION_ACCESS_TIME_UNIT;
import static $group__.$modId__.utilities.helpers.specific.Optionals.unboxOptional;
import static $group__.$modId__.utilities.helpers.specific.Throwables.*;

public interface IStrictEquals {
	/* SECTION static variables */

	Logger LOGGER = LogManager.getLogger(IStrictEquals.class);

	LoadingCache<Class<?>, BiFunction<Object, Object, Function<Function<Object, ? extends Boolean>, Boolean>>> FUNCTION_MAP = CacheBuilder.newBuilder().initialCapacity(INITIAL_CAPACITY_3).expireAfterAccess(CACHE_EXPIRATION_ACCESS_DURATION, CACHE_EXPIRATION_ACCESS_TIME_UNIT).concurrencyLevel(MULTI_THREAD_THREAD_COUNT).build(CacheLoader.from(k -> {
		assert k != null;

		ArrayList<BiFunction<Object, Object, Function<Function<Object, ? extends Boolean>, Boolean>>> efs =
				new ArrayList<>(INITIAL_CAPACITY_2);

		getThisAndSuperclasses(k).forEach(c -> {
			for (Field f : c.getDeclaredFields()) {
				if (isMemberStatic(f) || !isMemberFinal(f)) continue;
				@Nullable MethodHandle fm = unboxOptional(tryCall(() -> IMPL_LOOKUP.unreflectGetter(f), LOGGER));
				consumeIfCaughtThrowable(t -> LOGGER.warn(() -> SUFFIX_WITH_THROWABLE.makeMessage(INVOCATION_UNABLE_TO_UNREFLECT_MEMBER.makeMessage("field getter", f, IMPL_LOOKUP), t)));
				if (fm == null) continue;

				efs.add((t, o) -> z -> tryCallWithLogging(() -> Objects.equals(fm.invoke(t), fm.invoke(o)), LOGGER).orElse(true));
			}
		});
		if (efs.isEmpty()) return (t, o) -> z -> t == o;

		// COMMENT prepend
		efs.add(0,
				(t, o) -> z -> getLowerAndIntermediateSuperclasses(o.getClass(), castUncheckedUnboxedNonnull(k)).stream().allMatch(ic -> Arrays.stream(ic.getDeclaredFields()).allMatch(Dynamics::isMemberStatic)));
		efs.add(0, (t, o) -> z -> k.isAssignableFrom(o.getClass()));
		if (k.getSuperclass() != Object.class) efs.add(0, (t, o) -> z -> z.apply(o));

		return (t, o) -> z -> t == o || efs.stream().allMatch(ef -> ef.apply(t, o).apply(z));
	}));


	/* SECTION static methods */

	static <T, O> boolean areEqual(final T thisObj, O other, Function<? super O, ? extends Boolean> superEquals) { return FUNCTION_MAP.getUnchecked(thisObj.getClass()).apply(thisObj, other).apply(castUncheckedUnboxedNonnull(superEquals)); }


	/* SECTION methods */

	@Override
	@OverridingStatus(group = PACKAGE, when = When.ALWAYS)
	boolean equals(Object o);
}
