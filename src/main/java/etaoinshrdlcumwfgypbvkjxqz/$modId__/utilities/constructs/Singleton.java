package etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.annotation.Nullable;
import java.util.Map;

import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Miscellaneous.Casts.castUncheckedNullable;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Throwables.*;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.variables.References.LOGGER;

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

	protected static final Map<Class<?>, Singleton> INSTANCES = Maps.newConcurrentMap();
	protected static final Map<Class<?>, String> STACKTRACE_STRINGS = Maps.newConcurrentMap();


	/* SECTION static methods */

	@Nullable
	public static <T extends Singleton> T getInstance(Class<T> clazz, boolean instantiation) {
		return castUncheckedNullable(INSTANCES.computeIfAbsent(clazz, t -> {
			try { return instantiation ? castUnchecked(t.newInstance()) : null; } catch (InstantiationException | IllegalAccessException e) { throw rejectArguments(e, clazz); }
		}));
	}

	@SuppressWarnings("ConstantConditions")
	public static <T extends Singleton> T getInstance(Class<T> clazz) { return getInstance(clazz, true); }
}