package $group__.$modId__.utilities.constructs.classes;

import $group__.$modId__.utilities.variables.Globals;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;

import static $group__.$modId__.utilities.helpers.Casts.castUnchecked;
import static $group__.$modId__.utilities.helpers.Optionals.unboxOptional;
import static $group__.$modId__.utilities.helpers.Reflections.Unsafe.newInstance;
import static $group__.$modId__.utilities.helpers.Throwables.getStackTraceString;
import static $group__.$modId__.utilities.helpers.Throwables.rejectInstantiation;
import static $group__.$modId__.utilities.variables.Globals.LOGGER;

public abstract class Singleton {
	/* SECTION constructors */

	public Singleton() {
		Class<? extends Singleton> clazz = getClass();
		String classGS = clazz.toGenericString();
		String sts = getStackTraceString();

		synchronized (INSTANCES) {
			if (INSTANCES.put(clazz, this) != null) {
				LOGGER.error("Singleton instance of '{}' already created, previous stacktrace:\n{}", classGS, STACKTRACE_STRINGS.get(clazz));
				throw rejectInstantiation();
			}
			STACKTRACE_STRINGS.put(clazz, sts);
		}

		LOGGER.debug("Singleton instance of '{}' created, stacktrace:\n{}", classGS, sts);
	}


	/* SECTION static variables */

	protected static final HashMap<Class<?>, Singleton> INSTANCES = new HashMap<>();
	protected static final HashMap<Class<?>, String> STACKTRACE_STRINGS = new HashMap<>();


	/* SECTION static methods */

	public static <T extends Singleton> Optional<T> getInstance(Class<T> clazz, boolean instantiate, Function<? super Class<T>, ? extends T> instantiation) {
		Optional<T> r;
		synchronized (INSTANCES) {
			r = castUnchecked(INSTANCES.get(clazz));
			if (!r.isPresent()) r = Optional.ofNullable(instantiate ? instantiation.apply(clazz) : null);
		}
		return r;
	}

	public static <T extends Singleton> T getInstance(Class<T> clazz) { return getInstance(clazz, true, t -> unboxOptional(newInstance(t))).orElseThrow(Globals::rethrowCaughtThrowableStatic); }
}
