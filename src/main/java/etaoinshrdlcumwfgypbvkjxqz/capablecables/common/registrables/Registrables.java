package etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrables;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.Singleton;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.lang.reflect.Field;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.CapableCables.LOGGER;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.GrammarHelper.appendPluralSuffix;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.wrapUnhandledThrowable;

@SuppressWarnings("SpellCheckingInspection")
public abstract class Registrables<T extends IForgeRegistryEntry<T>> extends Singleton {
    private final Class<T> clazz;
    public Registrables(Class<T> clazz) {
        this.clazz = clazz;
    }

    @SuppressWarnings("unchecked")
    protected void register(RegistryEvent.Register<T> e) {
        String classGS = clazz.toGenericString();
        LOGGER.info("Registration of '{}' started", classGS);
        IForgeRegistry<T> reg = e.getRegistry();
        int reged = 0;

        {
            Object v;
            Field[] fs = getClass().getFields();
            LOGGER.info("Found {} field{} in '{}'", fs.length, appendPluralSuffix(fs.length, "s"), classGS);
            for (Field f : fs) {
                try {
                    v = f.get(this);
                } catch (IllegalAccessException ex) {
                    throw wrapUnhandledThrowable(ex, String.format("Unexpected illegal access to '%s'", f.toGenericString()));
                }
                LOGGER.debug("Field '{}' value is '{}'", f.toGenericString(), v);
                if (clazz.isAssignableFrom(v.getClass())) {
                    reg.register(((T) v));
                    reged++;
                    LOGGER.debug("Registered '{}'", v.toString());
                }
            }
        }

        LOGGER.info("Registered {} '{}'", reged, classGS);
        LOGGER.info("Registration of '{}' ended", classGS);
    }
}
