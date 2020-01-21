package $group__.$modId__.common.registrable;

import $group__.$modId__.common.registrable.utilities.constructs.IEventBusSubscriber;
import $group__.$modId__.utilities.constructs.classes.Singleton;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.lang.reflect.Field;

import static $group__.$modId__.utilities.helpers.Casts.castUnchecked;
import static $group__.$modId__.utilities.helpers.Grammar.appendSuffixIfPlural;
import static $group__.$modId__.utilities.helpers.Reflections.getGenericSuperclassActualTypeArgument;
import static $group__.$modId__.utilities.helpers.Throwables.unexpected;
import static $group__.$modId__.utilities.variables.References.LOGGER;

@SuppressWarnings("SpellCheckingInspection")
public abstract class Registerable<T extends IForgeRegistryEntry<T>> extends Singleton {
	/* SECTION variables */

	protected final Class<T> clazz;


	/* SECTION constructors */

	protected Registerable() { clazz = getGenericSuperclassActualTypeArgument(getClass(), 0); }


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
				try { v = f.get(this); } catch (IllegalAccessException ex) { throw unexpected(ex); }
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
