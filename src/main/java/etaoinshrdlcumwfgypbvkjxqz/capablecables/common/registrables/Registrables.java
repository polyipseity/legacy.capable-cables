package etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrables;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrables.utilities.IEventBusSubscriber;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.Singleton;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.concurrent.Immutable;
import java.lang.reflect.Field;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.CapableCables.LOGGER;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.GrammarHelper.appendSuffixIfPlural;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.wrapUnhandledThrowable;

@SuppressWarnings("SpellCheckingInspection")
@Immutable
public abstract class Registrables<T extends IForgeRegistryEntry<T>> extends Singleton {
    private final Class<T> clazz;
    public Registrables(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    @SuppressWarnings("unchecked")
    protected void register(RegistryEvent.Register<T> e) {
        String classGS = clazz.toGenericString();
        LOGGER.info("Registration of '{}' started", classGS);
        IForgeRegistry<T> reg = e.getRegistry();
        int reged = 0;
        int evtReged = 0;
        String evtRegSName = Mod.EventBusSubscriber.class.getSimpleName();

        {
            Object v;
            Field[] fs = getClass().getFields();
            LOGGER.info("Found {} field{} in '{}'", fs.length, appendSuffixIfPlural(fs.length, "s"), classGS);
            for (Field f : fs) {
                try {
                    v = f.get(this);
                } catch (IllegalAccessException ex) {
                    throw wrapUnhandledThrowable(ex, String.format("Unexpected illegal access to '%s'", f.toGenericString()));
                }
                LOGGER.debug("Field '{}' value is '{}'", f.toGenericString(), v);
                if (clazz.isAssignableFrom(v.getClass())) {
                    reg.register((T)v);
                    if (v instanceof IEventBusSubscriber) {
                        MinecraftForge.EVENT_BUS.register(v);
                        evtReged++;
                        LOGGER.debug("Registered '{}' as '{}'", v, evtRegSName);
                    }
                    reged++;
                    LOGGER.debug("Registered '{}' as '{}'", v, classGS);
                }
            }
        }

        LOGGER.info("Registered {} '{}'", reged, classGS);
        LOGGER.info("Registered {} '{}'", evtReged, evtRegSName);
        LOGGER.info("Registration of '{}' ended", classGS);
    }
}
