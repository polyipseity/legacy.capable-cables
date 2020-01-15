package etaoinshrdlcumwfgypbvkjxqz.$modId__.common.registrable;

import etaoinshrdlcumwfgypbvkjxqz.$modId__.common.registrable.utilities.constructs.IEventBusSubscriber;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.Singleton;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.lang.reflect.Field;

import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Grammar.appendSuffixIfPlural;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Miscellaneous.Reflections.getGenericSuperclassActualTypeArgument;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Throwables.unexpectedThrowable;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.variables.References.LOGGER;

public abstract class Registrable<T extends IForgeRegistryEntry<T>> extends Singleton {
	/* SECTION variables */

	protected final Class<T> clazz;


	/* SECTION constructors */

	protected Registrable() { clazz = getGenericSuperclassActualTypeArgument(getClass(), 0); }


	/* SECTION methods */

	@OverridingMethodsMustInvokeSuper
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
					reg.register(castUnchecked(v));
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
