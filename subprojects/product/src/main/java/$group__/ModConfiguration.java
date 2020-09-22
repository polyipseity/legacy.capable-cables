package $group__;

import $group__.utilities.structures.Singleton;
import $group__.utilities.templates.CommonConfigurationTemplate;
import $group__.utilities.throwable.IThrowableHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.function.Supplier;

import static $group__.ModConstants.MOD_ID;

public final class ModConfiguration
		extends CommonConfigurationTemplate<ModConfiguration.ConfigurationData> {
	private static final Logger BOOTSTRAP_LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static final ModConfiguration INSTANCE = Singleton.getSingletonInstance(ModConfiguration.class);

	private ModConfiguration() { super(getBootstrapLogger()); }

	public static Logger getBootstrapLogger() { return BOOTSTRAP_LOGGER; }

	public static ModConfiguration getInstance() { return INSTANCE; }

	public static final class ConfigurationData
			extends CommonConfigurationTemplate.ConfigurationData {
		public ConfigurationData(Logger logger,
		                         IThrowableHandler<Throwable> throwableHandler,
		                         Supplier<? extends Locale> localeSupplier) {
			super(logger, throwableHandler, localeSupplier);
		}
	}
}
