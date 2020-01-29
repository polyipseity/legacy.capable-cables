package $group__.$modId__.common.registrable;

import $group__.$modId__.common.registrable.utilities.constructs.IRegistrableEventBusSubscriber;
import $group__.$modId__.utilities.constructs.classes.Singleton;
import $group__.$modId__.utilities.helpers.Reflections.Unsafe.AccessibleObjectAdapter.FieldAdapter;
import $group__.$modId__.utilities.variables.Globals;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.lang.reflect.Field;

import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Grammar.appendSuffixIfPlural;
import static $group__.$modId__.utilities.helpers.Reflections.getGenericSuperclassActualTypeArgument;
import static $group__.$modId__.utilities.variables.Globals.LOGGER;

public abstract class Registrable<T extends IForgeRegistryEntry<T>> extends Singleton {
	/* SECTION variables */

	protected final Class<T> clazz;


	/* SECTION constructors */

	protected Registrable() { clazz = castUncheckedUnboxedNonnull(getGenericSuperclassActualTypeArgument(getClass(), 0)); }


	/* SECTION methods */

	@OverridingMethodsMustInvokeSuper
	protected void register(RegistryEvent.Register<T> event) {
		String classGS = clazz.toGenericString(),
				evtRegGS = IRegistrableEventBusSubscriber.class.toGenericString();
		LOGGER.info("Registration of '{}' started", classGS);
		IForgeRegistry<T> reg = event.getRegistry();

		int regEd = 0,
				evtRegEd = 0;
		{
			Object v;
			Field[] fs = getClass().getFields();
			LOGGER.info("Found {} field{} in '{}'", fs.length, appendSuffixIfPlural(fs.length, "s"), classGS);
			for (Field f : fs) {
				LOGGER.trace("Processing field '{}'", f.toGenericString());
				v = FieldAdapter.of(f).get_(this).orElseThrow(Globals::rethrowCaughtThrowableStatic);
				LOGGER.debug("Field '{}' value is '{}'", f.toGenericString(), v);
				if (clazz.isAssignableFrom(v.getClass())) {
					reg.register(castUncheckedUnboxedNonnull(v));
					if (v instanceof IRegistrableEventBusSubscriber) {
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
