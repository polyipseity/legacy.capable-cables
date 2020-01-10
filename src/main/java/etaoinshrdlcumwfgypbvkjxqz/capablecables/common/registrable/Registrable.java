package etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.utilities.constructs.IEventBusSubscriber;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.constructs.Singleton;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.lang.reflect.Field;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.GrammarHelper.appendSuffixIfPlural;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Throwables.unexpectedThrowable;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Types.getGenericSuperclassActualTypeArgument;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.variables.References.LOGGER;

public abstract class Registrable<T extends IForgeRegistryEntry<T>> extends Singleton {
	protected final Class<T> clazz;


	protected Registrable() { this.clazz = getGenericSuperclassActualTypeArgument(getClass(), 0); }


	@SuppressWarnings("unchecked")
	protected void register(RegistryEvent.Register<T> e) {
		String classGS = clazz.toGenericString();
		LOGGER.info("Registration of '{}' started", classGS);
		IForgeRegistry<T> reg = e.getRegistry();
		int regEd = 0;
		int evtRegEd = 0;
		String evtRegGS = Mod.EventBusSubscriber.class.toGenericString();

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
						LOGGER.debug("Registered '{}' as '{}'", v, evtRegGS);
					}
					regEd++;
					LOGGER.debug("Registered '{}' as '{}'", v, classGS);
				}
			}
		}

		LOGGER.info("Registered {} '{}'", regEd, classGS);
		LOGGER.info("Registered {} '{}'", evtRegEd, evtRegGS);
		LOGGER.info("Registration of '{}' ended", classGS);
	}
}
