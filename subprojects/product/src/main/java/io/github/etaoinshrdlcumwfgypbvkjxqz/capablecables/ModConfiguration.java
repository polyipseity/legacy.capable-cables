package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.throwable.IThrowableHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.function.Supplier;

public final class ModConfiguration
		extends CommonConfigurationTemplate<ModConfiguration.ConfigurationData> {
	private static final Logger BOOTSTRAP_LOGGER = LoggerFactory.getLogger(ModConstants.MOD_ID);
	private static final Supplier<ModConfiguration> INSTANCE = Suppliers.memoize(ModConfiguration::new);

	private ModConfiguration() {}

	public static Logger getBootstrapLogger() { return BOOTSTRAP_LOGGER; }

	public static ModConfiguration getInstance() { return AssertionUtilities.assertNonnull(INSTANCE.get()); }

	public static final class ConfigurationData
			extends CommonConfigurationTemplate.ConfigurationData {
		public ConfigurationData(Logger logger,
		                         IThrowableHandler<Throwable> throwableHandler,
		                         Supplier<? extends Locale> localeSupplier) {
			super(logger, throwableHandler, localeSupplier);
		}
	}
}
