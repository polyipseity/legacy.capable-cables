package io.github.etaoinshrdlcumwfgypbvkjxqz.common.registrables;

import io.github.etaoinshrdlcumwfgypbvkjxqz.utilities.Singleton;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.lang.reflect.Field;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.CapableCables.LOGGER;
import static io.github.etaoinshrdlcumwfgypbvkjxqz.utilities.ThrowableHelper.wrapUnhandledThrowable;

@SuppressWarnings("SpellCheckingInspection")
public abstract class Registrables<T extends IForgeRegistryEntry<T>> extends Singleton {
    private final Class<T> clazz;
    public Registrables(final Class<T> clazz) {
        this.clazz = clazz;
    }

    @SuppressWarnings("unchecked")
    protected void register(final RegistryEvent.Register<T> e) {
        LOGGER.info("Registration of '{}' started", clazz.toGenericString());
        IForgeRegistry<T> reg = e.getRegistry();
        Object v;
        int reged = 0;
        Field[] fs = getClass().getFields();
        LOGGER.info("Found {} field{} in {}", fs.length, fs.length == 1 ? "" : "s", clazz.toGenericString());
        for (final Field f : fs) {
            try { v = f.get(this); } catch (IllegalAccessException ex) { throw wrapUnhandledThrowable(ex, String.format("Unexpected illegal access to '%s'", f.toGenericString())); }
            LOGGER.debug("Field '{}' value is '{}'", f.toGenericString(), v);
            if (clazz.isAssignableFrom(v.getClass())) {
                reg.register(((T)v));
                reged++;
                LOGGER.debug("Registered '{}'", v.toString());
            }
        }
        LOGGER.info("Registered {} '{}'", reged, clazz.toGenericString());
        LOGGER.info("Registration of '{}' ended", clazz.toGenericString());
    }
}
