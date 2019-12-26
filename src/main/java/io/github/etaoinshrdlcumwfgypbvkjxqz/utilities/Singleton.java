package io.github.etaoinshrdlcumwfgypbvkjxqz.utilities;

import com.google.common.collect.Maps;

import java.util.Map;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.CapableCables.LOGGER;
import static io.github.etaoinshrdlcumwfgypbvkjxqz.utilities.ThrowableHelper.rejectInstantiation;

public abstract class Singleton {
    private static final Map<Class<?>, Object> INSTANCES = Maps.newConcurrentMap();
    public Singleton() {
        if (INSTANCES.put(getClass(), this) != null) throw rejectInstantiation();
        LOGGER.debug("Singleton instance of '{}' created", getClass().toGenericString());
    }

    @SuppressWarnings("unchecked")
    public static <T> T getInstance(Class<T> clazz) {
        try {
            return INSTANCES.containsKey(clazz) ? (T)INSTANCES.get(clazz) : clazz.newInstance();
        } catch (Exception ex) {
            throw ThrowableHelper.wrapUnhandledThrowable(new IllegalArgumentException(String.format("Illegal arguments passed: %s", clazz.toGenericString()), ex));
        }
    }
}
