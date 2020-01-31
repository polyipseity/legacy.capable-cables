package $group__.$modId__.utilities.constructs.classes;

import $group__.$modId__.utilities.helpers.Casts;
import $group__.$modId__.utilities.variables.Globals;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

import static $group__.$modId__.utilities.helpers.MapsExtension.MULTI_THREAD_MAP_MAKER;
import static $group__.$modId__.utilities.helpers.Optionals.unboxOptional;
import static $group__.$modId__.utilities.helpers.Reflections.Unsafe.newInstance;
import static $group__.$modId__.utilities.helpers.Throwables.getStackTraceString;
import static $group__.$modId__.utilities.helpers.Throwables.rejectInstantiation;
import static $group__.$modId__.utilities.variables.Globals.LOGGER;
import static com.google.common.collect.Maps.immutableEntry;

public abstract class Singleton {
	/* SECTION static variables */

	protected static final ConcurrentMap<Class<?>, Map.Entry<? extends Singleton, String>> INSTANCES = MULTI_THREAD_MAP_MAKER.makeMap();


	/* SECTION constructors */

	protected Singleton() {
		Class<? extends Singleton> clazz = getClass();
		String classGS = clazz.toGenericString(),
				sts = getStackTraceString();

		Map.Entry<? extends Singleton, String> v = immutableEntry(this, sts);
		@Nullable Map.Entry<? extends Singleton, String> vo = INSTANCES.put(clazz, v);
		if (vo != null) {
			LOGGER.error("Singleton instance of '{}' already created, previous stacktrace:\n{}", classGS, vo.getValue());
			throw rejectInstantiation();
		}

		LOGGER.debug("Singleton instance of '{}' created, stacktrace:\n{}", classGS, sts);
	}


	/* SECTION static methods */

	public static <T extends Singleton> T getInstance(Class<T> clazz) { return getInstance(clazz, true, t -> unboxOptional(newInstance(t))).orElseThrow(Globals::rethrowCaughtThrowableStatic); }

	public static <T extends Singleton> Optional<T> getInstance(Class<T> clazz, boolean instantiate, Function<? super Class<T>, ? extends T> instantiation) {
		Optional<T> r = Optional.ofNullable(INSTANCES.get(clazz)).map(Map.Entry::getKey).flatMap(Casts::castUnchecked);
		if (!r.isPresent() && instantiate) r = Optional.of(instantiation.apply(clazz));
		return r;
	}
}
