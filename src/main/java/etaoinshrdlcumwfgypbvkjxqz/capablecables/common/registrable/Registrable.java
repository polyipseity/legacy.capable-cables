package etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.utilities.IEventBusSubscriber;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.Singleton;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.lang.reflect.Field;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.CapableCables.LOGGER;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.GrammarHelper.appendSuffixIfPlural;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.unexpectedThrowable;

public abstract class Registrable<T extends IForgeRegistryEntry<T>> extends Singleton {
    private final Class<T> clazz;
    public Registrable(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    @SuppressWarnings("unchecked")
    protected void register(RegistryEvent.Register<T> e) {
        String classGS = clazz.toGenericString();
        LOGGER.info("Registration of '{}' started", classGS);
        IForgeRegistry<T> reg = e.getRegistry();
        int regEd = 0;
        int evtRegEd = 0;
        String evtRegSName = Mod.EventBusSubscriber.class.getSimpleName();

        {
            Object v;
            Field[] fs = getClass().getFields();
            LOGGER.info("Found {} field{} in '{}'", fs.length, appendSuffixIfPlural(fs.length, "s"), classGS);
            for (Field f : fs) {
                LOGGER.trace("Processing field '{}'", f.toGenericString());
                try { v = f.get(this); } catch (IllegalAccessException ex) { throw unexpectedThrowable(ex); }
                LOGGER.debug("Field '{}' value is '{}'", f.toGenericString(), v);
                if (clazz.isAssignableFrom(v.getClass())) {
                    reg.register((T) v);
                    if (v instanceof IEventBusSubscriber) {
                        MinecraftForge.EVENT_BUS.register(v);
                        evtRegEd++;
                        LOGGER.debug("Registered '{}' as '{}'", v, evtRegSName);
                    }
                    regEd++;
                    LOGGER.debug("Registered '{}' as '{}'", v, classGS);
                }
            }
        }

        LOGGER.info("Registered {} '{}'", regEd, classGS);
        LOGGER.info("Registered {} '{}'", evtRegEd, evtRegSName);
        LOGGER.info("Registration of '{}' ended", classGS);
    }
}
