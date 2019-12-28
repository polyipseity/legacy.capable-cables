package etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import com.google.common.collect.Maps;

import javax.annotation.concurrent.Immutable;
import java.util.Map;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.CapableCables.LOGGER;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.rejectArguments;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.rejectInstantiation;

@Immutable
public abstract class Singleton {
    private static final Map<Class<?>, Object> INSTANCES = Maps.newConcurrentMap();
    public Singleton() {
        if (INSTANCES.put(getClass(), this) != null) throw rejectInstantiation();
        LOGGER.debug("Singleton instance of '{}' created", getClass().toGenericString());
    }

    @SuppressWarnings("unchecked")
    public static <T extends Singleton> T getInstance(Class<T> clazz) {
        try {
            return INSTANCES.containsKey(clazz) ? (T)INSTANCES.get(clazz) : clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException ex) {
            throw rejectArguments(ex, clazz);
        }
    }
}
