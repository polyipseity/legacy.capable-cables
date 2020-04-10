package $group__.$modId__.common.registrables;

import $group__.$modId__.common.registrables.traits.IRegistrableEventBusSubscriber;
import $group__.$modId__.utilities.Singleton;
import $group__.$modId__.utilities.helpers.specific.Throwables;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.Logger;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.lang.reflect.Field;

import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Dynamics.getGenericSuperclassActualTypeArgument;
import static $group__.$modId__.utilities.helpers.Grammar.appendSuffixIfPlural;
import static $group__.$modId__.utilities.helpers.specific.Loggers.EnumMessages.FACTORY_PARAMETERIZED_MESSAGE;
import static $group__.$modId__.utilities.helpers.specific.Throwables.tryCall;

public abstract class Registrable<T extends IForgeRegistryEntry<T>> extends Singleton {
	/* SECTION variables */

	protected final Class<T> clazz;


	/* SECTION constructors */

	protected Registrable(Logger logger) {
		super(logger);
		clazz = castUncheckedUnboxedNonnull(getGenericSuperclassActualTypeArgument(getClass(), 0));
	}


	/* SECTION methods */

	@OverridingMethodsMustInvokeSuper
	protected void register(RegistryEvent.Register<T> event, @SuppressWarnings("SameParameterValue") Logger logger) {
		String classGS = clazz.toGenericString(),
				evtRegGS = IRegistrableEventBusSubscriber.class.toGenericString();
		logger.info(() -> FACTORY_PARAMETERIZED_MESSAGE.makeMessage("Registration of '{}' started", classGS));
		IForgeRegistry<T> reg = event.getRegistry();

		int regEd = 0,
				evtRegEd = 0;

		Field[] fs = getClass().getFields();
		logger.info(() -> FACTORY_PARAMETERIZED_MESSAGE.makeMessage("Found {} field{} in '{}'", fs.length,
				appendSuffixIfPlural(fs.length, "s"), classGS));
		for (Field f : fs) {
			logger.trace(() -> FACTORY_PARAMETERIZED_MESSAGE.makeMessage("Processing field '{}'",
					f.toGenericString()));
			Object v = tryCall(() -> f.get(this), logger).orElseThrow(Throwables::rethrowCaughtThrowableStatic);
			logger.debug(() -> FACTORY_PARAMETERIZED_MESSAGE.makeMessage("Field '{}' value is '{}'",
					f.toGenericString(), v));
			if (clazz.isAssignableFrom(v.getClass())) {
				reg.register(castUncheckedUnboxedNonnull(v));
				if (v instanceof IRegistrableEventBusSubscriber) {
					MinecraftForge.EVENT_BUS.register(v);
					++evtRegEd;
					logger.debug(() -> FACTORY_PARAMETERIZED_MESSAGE.makeMessage("Registered '{}' as '{}'", v,
							evtRegGS));
				}
				++regEd;
				logger.debug(() -> FACTORY_PARAMETERIZED_MESSAGE.makeMessage("Registered '{}' as '{}'", v, classGS));
			}
		}

		int regEdF = regEd, evtRegEdF = evtRegEd;
		logger.info(() -> FACTORY_PARAMETERIZED_MESSAGE.makeMessage("Registered {} '{}'", regEdF, classGS));
		logger.info(() -> FACTORY_PARAMETERIZED_MESSAGE.makeMessage("Registered {} '{}'", evtRegEdF, evtRegGS));
		logger.info(() -> FACTORY_PARAMETERIZED_MESSAGE.makeMessage("Registration of '{}' ended", classGS));
	}
}
