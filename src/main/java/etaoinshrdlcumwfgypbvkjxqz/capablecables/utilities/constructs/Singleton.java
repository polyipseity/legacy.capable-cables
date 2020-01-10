package etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.constructs;

import com.google.common.collect.Maps;

import java.util.Map;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Throwables.rejectArguments;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Throwables.rejectInstantiation;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.variables.References.LOGGER;

public abstract class Singleton {
	private static final Map<Class<?>, Singleton> INSTANCES = Maps.newConcurrentMap();

	public Singleton() {
		if (INSTANCES.put(getClass(), this) != null) throw rejectInstantiation();
		LOGGER.debug("Singleton instance of '{}' created", getClass().toGenericString());
	}

	@SuppressWarnings("unchecked")
	public static <T extends Singleton> T getInstance(Class<T> clazz) {
		try {
			return INSTANCES.containsKey(clazz) ? (T) INSTANCES.get(clazz) : clazz.newInstance();
		} catch (IllegalAccessException | InstantiationException ex) {
			throw rejectArguments(ex, clazz);
		}
	}
}
