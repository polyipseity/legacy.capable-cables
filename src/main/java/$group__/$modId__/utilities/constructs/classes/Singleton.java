package $group__.$modId__.utilities.constructs.classes;

import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentHashMap;

import static $group__.$modId__.utilities.helpers.Casts.castUnchecked;
import static $group__.$modId__.utilities.helpers.Throwables.*;
import static $group__.$modId__.utilities.variables.References.LOGGER;

public abstract class Singleton {
	/* SECTION constructors */

	public Singleton() {
		Class<? extends Singleton> clazz = getClass();
		String classGS = clazz.toGenericString();

		if (INSTANCES.put(clazz, this) != null) {
			LOGGER.error("Singleton instance of '{}' already created, previous stacktrace: {}", classGS, STACKTRACE_STRINGS.get(clazz));
			throw rejectInstantiation();
		}
		String sts = ExceptionUtils.getStackTrace(newThrowable());
		STACKTRACE_STRINGS.put(clazz, sts);

		LOGGER.info("Singleton instance of '{}' created", classGS);
		LOGGER.debug("Singleton instance of '{}' created, stacktrace: {}", classGS, sts);
	}


	/* SECTION static variables */

	protected static final ConcurrentHashMap<Class<?>, Singleton> INSTANCES = new ConcurrentHashMap<>();
	protected static final ConcurrentHashMap<Class<?>, String> STACKTRACE_STRINGS = new ConcurrentHashMap<>();


	/* SECTION static methods */

	@Nullable
	public static <T extends Singleton> T getInstance(Class<T> clazz, boolean instantiation) {
		try { return instantiation ? castUnchecked(clazz.newInstance()) : null; } catch (InstantiationException | IllegalAccessException e) { throw rejectArguments(e, clazz); }
	}

	@SuppressWarnings("ConstantConditions")
	public static <T extends Singleton> T getInstance(Class<T> clazz) { return getInstance(clazz, true); }
}
